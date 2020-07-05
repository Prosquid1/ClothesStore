package com.oyelekeokiki.ui.shared

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.oyelekeokiki.R
import com.oyelekeokiki.model.Product
import com.oyelekeokiki.ui.cart.OnCartModified

/**
 * Displays the products from the API, into a list of items.
 */

typealias OnWishListModified = (product: Product, isLiked: Boolean) -> Unit

class ProductAdapter(
    private val onWishListModified: OnWishListModified?,
    private val onCartModified: OnCartModified
) :
    RecyclerView.Adapter<ProductViewHolder>() {

    private val data: MutableList<Product> = mutableListOf()
    private var likedProductIds: List<Int> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_list_item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(productViewHolder: ProductViewHolder, position: Int) {
        val product = data[position]
        val productIsInWishList = likedProductIds.contains(product.id)
        productViewHolder.bindData(
            product,
            onWishListModified, onCartModified, productIsInWishList
        )
    }

    fun setData(data: List<Product>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    fun setWishListIds(likedIds: List<Int>) {
        likedProductIds = likedIds
    }
}