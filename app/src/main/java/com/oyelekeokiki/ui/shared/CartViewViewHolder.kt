package com.oyelekeokiki.ui.shared

import android.annotation.SuppressLint
import android.view.View
import com.oyelekeokiki.model.Product
import kotlinx.android.synthetic.main.recycler_list_item_product.view.*

@SuppressLint("SetTextI18n")
class CartViewViewHolder(override val containerView: View) : ProductViewHolder(containerView) {
    override fun bindData(
        product: Product,
        onWishListModified: OnWishListModified?,
        onCartModified: OnCartModified?,
        productIsLiked: Boolean
    ) {
        super.bindData(product, onWishListModified, onCartModified, productIsLiked)
        containerView.add_to_cart_button.visibility = View.GONE
        containerView.add_to_wishlist_button.visibility = View.GONE
        containerView.remove_from_cart_button.visibility = View.VISIBLE

        containerView.remove_from_cart_button.setOnClickListener {
            onCartModified?.let {
                it(product.id.toString())
            }
        }

    }

}