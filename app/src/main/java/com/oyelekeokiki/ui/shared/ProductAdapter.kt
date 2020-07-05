package com.oyelekeokiki.ui.shared

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.oyelekeokiki.R
import com.oyelekeokiki.model.Product

/**
 * Displays the products from the API, into a list of items.
 */

typealias onWishListModified = (product: Product, isLiked: Boolean) -> Unit
typealias onCartModified = (productId: String, wasAdded: Boolean ) -> Unit

class ProductAdapter(private val onWishListModified: onWishListModified?,
                     private val onOnCartModified: onCartModified) :
    RecyclerView.Adapter<ProductViewHolder>() {

    private val data: MutableList<Product> = mutableListOf()
    private var likedProductIds: List<Int> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_list_item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(viewHolder: ProductViewHolder, position: Int) {
        val product = data[position]
        val productIsInWishList = likedProductIds.contains(product.id)
        viewHolder.bindData(product,
            onWishListModified, onOnCartModified, productIsInWishList)
    }

    fun addData(item: Product) {
        data.add(item)
        notifyItemInserted(data.size)
    }

    fun setData(data: List<Product>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    fun setWishListIds(likedIds: List<Int>) {
        likedProductIds = likedIds
    }

    fun removeProduct(productId: Int) {
        val productIndex = data.indexOfFirst { it.id == productId }

        if (productIndex != -1) {
            data.removeAt(productIndex)
            notifyItemRemoved(productIndex)
        }
    }
}