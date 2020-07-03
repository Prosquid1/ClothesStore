package com.oyelekeokiki.ui.shared

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.like.LikeButton
import com.like.OnLikeListener
import com.oyelekeokiki.R
import com.oyelekeokiki.model.Product
import kotlinx.android.extensions.LayoutContainer


/**
 * Holder to display the Product item in a grid or list.
 */

@SuppressLint("SetTextI18n")
class ProductHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
  LayoutContainer {

  private lateinit var addToWishListImageButton: LikeButton
  private lateinit var productCategoryTextView: TextView
  private lateinit var productNameTextView: TextView
  private lateinit var productPreviousPriceTextView: TextView
  private lateinit var productPriceTextView: TextView
  private lateinit var soldOutTextView: TextView
  private lateinit var stockCountTextView: TextView

  private fun bindRequiredViews() {
    addToWishListImageButton = containerView.findViewById(R.id.add_to_wishlist_button)
    productCategoryTextView = containerView.findViewById(R.id.product_category)
    productNameTextView = containerView.findViewById(R.id.product_name)
    productPreviousPriceTextView = containerView.findViewById(R.id.product_old_price)
    productPriceTextView = containerView.findViewById(R.id.product_price)
    soldOutTextView = containerView.findViewById(R.id.sold_out_text)
    stockCountTextView = containerView.findViewById(R.id.stock_count)
  }

  fun bindData(product: Product) {
    bindRequiredViews()
    productNameTextView.text = product.name
    productPriceTextView.text = "£${product.price}"
    setupOldPriceView(product.oldPrice)

    productCategoryTextView.text = product.category
    setupStockCount(product.stock)

    addToWishListImageButton.setOnLikeListener(object : OnLikeListener {
      override fun liked(likeButton: LikeButton) {}
      override fun unLiked(likeButton: LikeButton) {}
    })

  }

  private fun setupSoldOutView(soldOut: Boolean) {
    soldOutTextView.visibility =  if (soldOut) View.VISIBLE else View.GONE
  }

  private fun setupOldPriceView(oldPrice: String?) {
    if (oldPrice == null) {
      productPreviousPriceTextView.visibility =  View.GONE
      return
    }
    productPreviousPriceTextView.visibility =  View.VISIBLE
    productPreviousPriceTextView.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
    productPriceTextView.text = "£${oldPrice}"
  }

  private fun setupStockCount(count: Int) {
    val itemIsSoldOut = count == 0;
    stockCountTextView.text = if (itemIsSoldOut) "(Out of stock)" else "(${count} left)"
    containerView.alpha = if (itemIsSoldOut) 0.6f else 1.0f
    setupSoldOutView(itemIsSoldOut)
  }

}