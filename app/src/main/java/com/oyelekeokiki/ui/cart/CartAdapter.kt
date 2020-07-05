package com.oyelekeokiki.ui.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.oyelekeokiki.R
import com.oyelekeokiki.model.CartToProductItem
import com.oyelekeokiki.model.Product

/**
 * Displays the products from the API, into a list of items.
 */

typealias OnCartModified = (productId: String) -> Unit

class CartAdapter(
    private val onCartModified: OnCartModified
) :
    RecyclerView.Adapter<CartViewViewHolder>() {

    private val data: MutableList<CartToProductItem> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_list_item_product, parent, false)
        return CartViewViewHolder(view)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(cartViewViewHolder: CartViewViewHolder, position: Int) {
        val cartToProductItem = data[position]
        cartViewViewHolder.bindCartItem(
            cartToProductItem,
            null, onCartModified, false
        )
    }

    fun setData(data: List<CartToProductItem>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }
}