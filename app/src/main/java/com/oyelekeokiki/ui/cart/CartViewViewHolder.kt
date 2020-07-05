package com.oyelekeokiki.ui.cart

import android.annotation.SuppressLint
import android.view.View
import androidx.core.content.ContextCompat
import com.oyelekeokiki.R
import com.oyelekeokiki.helpers.StockCountPriority
import com.oyelekeokiki.model.CartToProductItem
import com.oyelekeokiki.ui.shared.ProductViewHolder
import kotlinx.android.synthetic.main.recycler_list_item_product.view.*

@SuppressLint("SetTextI18n")
class CartViewViewHolder(override val containerView: View) : ProductViewHolder(containerView) {
    fun bindCartItem(
        cartToProductItem: CartToProductItem,
        onCartModified: OnCartModified,
        productIsLiked: Boolean
    ) {
        super.bindData(
            cartToProductItem.product,
            null,
            onCartModified,
            productIsLiked
        )

        hideExtraViews()

        setupDeleteFromCartButton(cartToProductItem.cartItemIds[0].toString(), onCartModified)
        setupCartItemCountTextView(cartToProductItem.cartItemIds.size)
    }

    private fun hideExtraViews() {
        containerView.add_to_cart_button.visibility = View.GONE
        containerView.add_to_wishlist_button.visibility = View.GONE
        containerView.remove_from_cart_button.visibility = View.VISIBLE
    }

    private fun setupDeleteFromCartButton(firstCartItemId: String, onCartModified: OnCartModified) {
        containerView.remove_from_cart_button.setOnClickListener {
            onCartModified(firstCartItemId)
        }
    }

    private fun setupCartItemCountTextView(itemsCount: Int) {
        val itemString = containerView.context.getString(R.string.item_string)
        containerView.stock_count.text =
            if (itemsCount == 1) "($itemsCount $itemString)" else "($itemsCount ${itemString}s)"
        containerView.stock_count.setTextColor(
            ContextCompat.getColor(
                containerView.context,
                R.color.colorPrimary
            )
        )
    }

}