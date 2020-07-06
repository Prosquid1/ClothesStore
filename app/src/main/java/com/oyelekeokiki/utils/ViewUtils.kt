package com.oyelekeokiki.utils

import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.oyelekeokiki.R
import com.oyelekeokiki.ui.shared.CSItemAnimator

fun RecyclerView.configureCSRecycler() {
    val itemDecoration = DividerItemDecoration(
        context,
        DividerItemDecoration.VERTICAL
    )
    addItemDecoration(
        itemDecoration
    )
    itemAnimator = CSItemAnimator()
    layoutManager = LinearLayoutManager(context)
}

fun ViewGroup.showCSSnackBar(
    message: String,
    responseWithType: ActionResponseType? = null,
    action: ((View) -> Unit)? = null
) {
    val snackBarLength =
        if (responseWithType == ActionResponseType.SUCCESS) Snackbar.LENGTH_SHORT else Snackbar.LENGTH_LONG

    val snackbarActionColorId =
        if (responseWithType == ActionResponseType.SUCCESS) R.color.green else R.color.red

    val snackBar = Snackbar.make(
        this,
        message,
        snackBarLength
    )
    action.let {
        val snackbarActionMessage =
            if (responseWithType == ActionResponseType.SUCCESS) this.context.getString(
                R.string.undo
            ) else this.context.getString(
                R.string.retry
            )
        if (snackbarActionMessage.isEmpty()) return@let
        snackBar.setAction(snackbarActionMessage, it)
        snackBar.setActionTextColor(
            ContextCompat.getColor(
                context,
                snackbarActionColorId
            )
        )
    }

    snackBar.show()
}