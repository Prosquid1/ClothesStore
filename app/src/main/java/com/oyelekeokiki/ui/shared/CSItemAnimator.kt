package com.oyelekeokiki.ui.shared


import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.os.Build
import android.util.ArrayMap
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView


class CSItemAnimator : DefaultItemAnimator() {
    private val accelerateInterpolator = AccelerateInterpolator(2f)
    private val decelerateInterpolator = DecelerateInterpolator(2f)

    // Used to construct the new change animation based on where the previous one was at when it was interrupted.
    private val animatorMap: ArrayMap<RecyclerView.ViewHolder, AnimatorInfo> =
        ArrayMap()

    override fun canReuseUpdatedViewHolder(viewHolder: RecyclerView.ViewHolder): Boolean {
        // This allows our custom change animation on the contents of the holder instead
        // of the default behavior of replacing the viewHolder entirely
        return true
    }

    // Custom change animation to expand the item then shrink it back to its original size.
    // If a new change animation occurs on an item that is currently animating a change, we stop the
    // previous change and start the new one where the old one left off.
    override fun animateChange(
        oldHolder: RecyclerView.ViewHolder,
        newHolder: RecyclerView.ViewHolder,
        preInfo: ItemHolderInfo,
        postInfo: ItemHolderInfo
    ): Boolean {
        if (oldHolder !== newHolder) {
            // use default behavior if not re-using view holders
            return super.animateChange(oldHolder, newHolder, preInfo, postInfo)
        }

        // Check to see if there's a change animation already running on this item
        val runningInfo: AnimatorInfo? = animatorMap[newHolder]
        var prevAnimPlayTime: Long = 0
        var firstHalf = false
        if (runningInfo != null) {
            // The information we need to construct the new animators is whether we are in the 'first half'
            // (scaling the size up) and how far we are into whichever half is running
            firstHalf = runningInfo.zoomInAnimator != null && runningInfo.zoomInAnimator.isRunning
            prevAnimPlayTime =
                if (firstHalf) runningInfo.zoomInAnimator!!.currentPlayTime else runningInfo.zoomOutAnimator.currentPlayTime
            // done with previous animation - cancel it
            runningInfo.overallAnim.cancel()
        }
        val itemView = newHolder.itemView
        var scaleUpAnimator: ValueAnimator? = null
        val scaleDownAnimator: ValueAnimator
        if (runningInfo == null || firstHalf) {
            // The first part of the animation scales the view
            // Skip this phase if we're interrupting an animation that was already in the second phase.
            scaleUpAnimator =
                ValueAnimator.ofFloat(1f, MAX_SCALE)
            scaleUpAnimator.interpolator = accelerateInterpolator
            scaleUpAnimator.duration = DURATION_SCALE
            scaleUpAnimator.addUpdateListener(AnimatorUpdateListener { animation ->
                val scale = animation.animatedValue as Float
                itemView.scaleX = scale
                itemView.scaleY = scale
            })
            if (runningInfo != null) {
                scaleUpAnimator.currentPlayTime = prevAnimPlayTime
            }
        }
        scaleDownAnimator =
            ValueAnimator.ofFloat(MAX_SCALE, 1f)
        scaleDownAnimator.interpolator = decelerateInterpolator
        scaleDownAnimator.addUpdateListener { animation ->
            val scale = animation.animatedValue as Float
            itemView.scaleX = scale
            itemView.scaleY = scale
        }
        if (runningInfo != null && !firstHalf) {
            // If we're interrupting a previous second-phase animation, seek to that time
            scaleDownAnimator.currentPlayTime = prevAnimPlayTime
        }

        // Choreograph first and second half. First half may be null if we interrupted a second-phase animation
        val overallAnimation = AnimatorSet()
        if (scaleUpAnimator != null) {
            overallAnimation.playSequentially(scaleUpAnimator, scaleDownAnimator)
        } else {
            overallAnimation.play(scaleDownAnimator)
        }
        overallAnimation.addListener(object : AnimatorListenerAdapter() {
            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
            override fun onAnimationStart(animation: Animator) {
                itemView.translationZ = 1f
            }

            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
            override fun onAnimationEnd(animation: Animator) {
                itemView.translationZ = 0f
                dispatchAnimationFinished(newHolder)
                animatorMap.remove(newHolder)
            }
        })
        overallAnimation.start()

        // Store info about this animation to be re-used if a succeeding change event occurs while it's still running
        val runningAnimInfo =
            AnimatorInfo(
                overallAnimation,
                scaleUpAnimator,
                scaleDownAnimator
            )
        animatorMap.put(newHolder, runningAnimInfo)
        return true
    }

    override fun endAnimation(item: RecyclerView.ViewHolder) {
        super.endAnimation(item)
        if (!animatorMap.isEmpty()) {
            val numRunning: Int = animatorMap.size
            for (i in numRunning downTo 0) {
                if (item === animatorMap.keyAt(i)) {
                    animatorMap.valueAt(i).overallAnim.cancel()
                }
            }
        }
    }

    override fun isRunning(): Boolean {
        return super.isRunning() || !animatorMap.isEmpty()
    }

    override fun endAnimations() {
        super.endAnimations()
        if (!animatorMap.isEmpty()) {
            val numRunning: Int = animatorMap.size
            for (i in numRunning downTo 0) {
                animatorMap.valueAt(i).overallAnim.cancel()
            }
        }
    }

    // Holds child animator objects for any change animation. Used when a new change animation interrupts one already
    // in progress; the new one is constructed to start from where the previous one was at when the interruption occurred.
    private class AnimatorInfo internal constructor(
        val overallAnim: Animator,
        val zoomInAnimator: ValueAnimator?,
        val zoomOutAnimator: ValueAnimator
    )

    companion object {
        private const val MAX_SCALE = 3f
        private const val DURATION_SCALE: Long = 300
    }
}