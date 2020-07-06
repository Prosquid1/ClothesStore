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
import com.oyelekeokiki.helpers.StockCountHelper
import com.oyelekeokiki.helpers.formatPrice
import com.oyelekeokiki.model.CartItem
import com.oyelekeokiki.model.Product
import com.oyelekeokiki.ui.cart.OnCartModified
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.recycler_list_item_product.view.*


/**
 * Holder to display the Product item in a grid or list.
 */

const val MINIMUM_STOCK_THRESHOLD = 3

@SuppressLint("SetTextI18n")
open class ProductViewHolder(override val containerView: View) :
    RecyclerView.ViewHolder(containerView),
    LayoutContainer {

    open fun bindData(
        product: Product,
        onWishListModified: OnWishListModified?,
        onCartModified: OnCartModified?,
        productIsLiked: Boolean
    ) {
        containerView.product_name.text = product.name
        containerView.product_price.text = "£${product.price}"
        setupOldPriceView(product.oldPrice)

        containerView.product_category.text = product.category
        setupStockView(product.stock)
        setupProductImageView(product.name)
        onWishListModified?.let {
            setupLikeView(product, productIsLiked, it)
        }

        setupAddToCartView(product.id, onCartModified)
    }

    private fun setupLikeView(
        product: Product,
        productIsLiked: Boolean,
        onWishListModified: OnWishListModified
    ) {
        containerView.add_to_wishlist_button.isLiked = productIsLiked
        containerView.add_to_wishlist_button.setOnLikeListener(object : OnLikeListener {
            override fun liked(likeButton: LikeButton) {
                onWishListModified(product, true)
            }

            override fun unLiked(likeButton: LikeButton) {
                onWishListModified(product, false)
            }
        })
    }

    private fun setupAddToCartView(
        productId: Int,
        onCartModified: OnCartModified?
    ) {
        if (onCartModified == null) {
            return
        }
        containerView.add_to_cart_button.setOnClickListener {
            onCartModified(
                CartItem(
                    null, // Cart id is nullable since you cannot delete a newly added item to cart
                    productId
                )
            )
        }
    }

    // Product URL is not available so colors will be generated based on product name
    private fun setupProductImageView(productName: String) {
        containerView.product_image.setBackgroundResource(R.drawable.round_corner_image_layout)
        val drawable = containerView.product_image.background as GradientDrawable
        drawable.setColor(ColorHelper.generateColorFromText(productName))
    }

    private fun setupSoldOutView(soldOut: Boolean) {
        containerView.sold_out_text.visibility = if (soldOut) View.VISIBLE else View.GONE
    }

    private fun setupOldPriceView(oldPrice: String?) {
        if (oldPrice == null) {
            containerView.product_old_price.visibility = View.GONE
            return
        }
        containerView.product_old_price.visibility = View.VISIBLE
        containerView.product_old_price.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        containerView.product_old_price.text = oldPrice.toInt().formatPrice()
    }

    private fun setupLikeButton(soldOut: Boolean) {
        containerView.add_to_cart_button.isEnabled = !soldOut
    }

    private fun setupStockView(count: Int) {
        val stockCountHelper = StockCountHelper.getPriority(count)
        containerView.stock_count.text = StockCountHelper.getTextValue(count)
        containerView.add_to_cart_button.alpha = StockCountHelper.getDimAlphaValue(count)
        setupSoldOutView(stockCountHelper.isItemSoldOut())
        setupLikeButton(stockCountHelper.isItemSoldOut())

        setupStockTextColor(stockCountHelper)
    }

    private fun setupStockTextColor(helper: StockCountHelper) {
        containerView.stock_count.setTextColor(
            ContextCompat.getColor(
                containerView.context,
                helper.getColor()
            )
        )
    }

}