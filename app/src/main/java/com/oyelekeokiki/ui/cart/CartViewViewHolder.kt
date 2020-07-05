package com.oyelekeokiki.ui.cart

import android.annotation.SuppressLint
import android.view.View
import com.oyelekeokiki.model.CartToProductItem
import com.oyelekeokiki.model.Product
import com.oyelekeokiki.ui.shared.OnWishListModified
import com.oyelekeokiki.ui.shared.ProductViewHolder
import kotlinx.android.synthetic.main.recycler_list_item_product.view.*

@SuppressLint("SetTextI18n")
class CartViewViewHolder(override val containerView: View) : ProductViewHolder(containerView) {
    fun bindCartItem(
        cartToProductItem: CartToProductItem,
        onWishListModified: OnWishListModified?,
        onCartModified: OnCartModified?,
        productIsLiked: Boolean
    ) {
        super.bindData(cartToProductItem.product, onWishListModified, onCartModified, productIsLiked)
        containerView.add_to_cart_button.visibility = View.GONE
        containerView.add_to_wishlist_button.visibility = View.GONE
        containerView.remove_from_cart_button.visibility = View.VISIBLE

        containerView.remove_from_cart_button.setOnClickListener {
            onCartModified?.let {
                it(cartToProductItem.cartId.toString())
            }
        }

    }

}