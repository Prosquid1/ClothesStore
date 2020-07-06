package com.oyelekeokiki.ui.cart

import android.annotation.SuppressLint
import android.view.View
import androidx.core.content.ContextCompat
import com.oyelekeokiki.R
import com.oyelekeokiki.helpers.OnCartModified
import com.oyelekeokiki.model.CartItem
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

        setupDeleteFromCartButton(cartToProductItem, onCartModified)
        setupCartItemCountTextView(cartToProductItem.cartItemIds.size)
    }

    private fun hideExtraViews() {
        containerView.add_to_cart_button.visibility = View.GONE
        containerView.add_to_wishlist_button.visibility = View.GONE
        containerView.remove_from_cart_button.visibility = View.VISIBLE
    }

    private fun getCartItemToDelete(cartToProductItem: CartToProductItem): CartItem {
        val cartItemId = cartToProductItem.cartItemIds[0]
        val productId = cartToProductItem.product.id
        return CartItem(cartItemId, productId)
    }

    private fun setupDeleteFromCartButton(
        cartToProductItem: CartToProductItem,
        onCartModified: OnCartModified
    ) {
        val cartItemToDelete = getCartItemToDelete(cartToProductItem)
        containerView.remove_from_cart_button.setOnClickListener {
            onCartModified(cartItemToDelete)
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