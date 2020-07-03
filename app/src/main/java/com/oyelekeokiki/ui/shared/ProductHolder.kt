package com.oyelekeokiki.ui.shared

import android.annotation.SuppressLint
import android.graphics.Paint
import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.like.LikeButton
import com.like.OnLikeListener
import com.oyelekeokiki.R
import com.oyelekeokiki.helpers.ColorHelper
import com.oyelekeokiki.helpers.StockCountPriority
import com.oyelekeokiki.model.Product
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.recycler_list_item_product.view.*


/**
 * Holder to display the Product item in a grid or list.
 */

const val MINIMUM_STOCK_THRESHOLD = 3

@SuppressLint("SetTextI18n")
class ProductHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
  LayoutContainer {

  fun bindData(product: Product) {
    containerView.product_name.text = product.name
    containerView.product_price.text = "£${product.price}"
    setupOldPriceView(product.oldPrice)

    containerView.product_category.text = product.category
    setupStockView(product.stock)
    setupProductImageView()
  }

  private fun setupProductImageView() {
    containerView.product_image.setBackgroundResource(R.drawable.round_corner_image_layout)
    val drawable = containerView.product_image.background as GradientDrawable
    drawable.setColor(ColorHelper.getRandomColor())
  }

  private fun setupSoldOutView(soldOut: Boolean) {
    containerView.sold_out_text.visibility =  if (soldOut) View.VISIBLE else View.GONE
  }

  private fun setupOldPriceView(oldPrice: String?) {
    if (oldPrice == null) {
      containerView.product_old_price.visibility =  View.GONE
      return
    }
    containerView.product_old_price.visibility =  View.VISIBLE
    containerView.product_old_price.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
    containerView.product_price.text = "£${oldPrice}"
  }

  private fun setupLikeButton(soldOut: Boolean) {
    containerView.add_to_wishlist_button.isEnabled = !soldOut
    containerView.add_to_wishlist_button.setOnLikeListener(object : OnLikeListener {
      override fun liked(likeButton: LikeButton) {}
      override fun unLiked(likeButton: LikeButton) {}
    })
  }

  private fun setupStockView(count: Int) {
    val itemIsSoldOut = count == 0
    containerView.stock_count.text = if (itemIsSoldOut) "(Out of stock)" else "(${count} left)"
    containerView.alpha = if (itemIsSoldOut) 0.6f else 1.0f
    setupSoldOutView(itemIsSoldOut)
    setupLikeButton(itemIsSoldOut)

    val stockPriority = if (count >= MINIMUM_STOCK_THRESHOLD) StockCountPriority.MEDIUM else StockCountPriority.LOW
    setupStockTextColor(stockPriority)
  }

  private fun setupStockTextColor(priority: StockCountPriority) {
    containerView.stock_count.setTextColor(ContextCompat.getColor(containerView.context, priority.getColor()))
  }

}