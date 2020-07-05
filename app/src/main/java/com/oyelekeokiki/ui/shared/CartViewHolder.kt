package com.oyelekeokiki.ui.shared

import android.annotation.SuppressLint
import android.view.View
import com.oyelekeokiki.model.Product

@SuppressLint("SetTextI18n")
class CartViewHolder(override val containerView: View) : ProductHolder(containerView) {
    override fun bindData(
        product: Product,
        onWishListModified: onWishListModified,
        onOnCartModified: onCartModified?,
        productIsLiked: Boolean
    ) {
        super.bindData(product, onWishListModified, onOnCartModified, productIsLiked)
    }

}