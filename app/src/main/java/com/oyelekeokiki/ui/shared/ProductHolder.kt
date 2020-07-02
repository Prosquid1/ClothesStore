package com.oyelekeokiki.ui.shared

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.RecyclerView
import com.oyelekeokiki.R
import com.oyelekeokiki.model.Product
import kotlinx.android.extensions.LayoutContainer

/**
 * Holder to display the Product item in a grid or list.
 */

@SuppressLint("SetTextI18n")
class ProductHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
  LayoutContainer {

  private lateinit var productNameTextView: TextView
  private lateinit var productCategoryTextView: TextView
  private lateinit var stockCountTextView: TextView
  private lateinit var productPriceTextView: TextView
  private lateinit var productPreviousPriceTextView: TextView
  private lateinit var addToWishListImageButton: AppCompatImageButton

  private fun bindRequiredViews() {
    productNameTextView = TextView(containerView.findViewById(R.id.product_name))
    productCategoryTextView = TextView(containerView.findViewById(R.id.product_category))
    stockCountTextView = TextView(containerView.findViewById(R.id.stock_count))
    productPriceTextView = TextView(containerView.findViewById(R.id.product_price))
    productPreviousPriceTextView = TextView(containerView.findViewById(R.id.product_old_price))
    addToWishListImageButton = AppCompatImageButton(containerView.findViewById(R.id.add_to_wishlist_image))
  }


  fun bindData(product: Product) {
    bindRequiredViews()
    productNameTextView.text = product.name
    productPriceTextView.text = "£${product.price}"
    setupOldPriceView(product.oldPrice)

    productCategoryTextView.text = product.category
    setupStockCount(product.stock)
    addToWishListImageButton.setOnClickListener {  }

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
    if (count == 0) {
      stockCountTextView.text =  "(Out of stock)"
      containerView.alpha = 0.6f;
      return
    }

    containerView.alpha = 1f;
    stockCountTextView.text = "(${count} left)"
  }

}