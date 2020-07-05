package com.oyelekeokiki.ui.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.oyelekeokiki.R
import com.oyelekeokiki.model.Product

/**
 * Displays the products from the API, into a list of items.
 */

typealias OnCartModified = (productId: String) -> Unit

class CartAdapter(
    private val onCartModified: OnCartModified
) :
    RecyclerView.Adapter<CartViewViewHolder>() {

    private val data: MutableList<Product> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_list_item_product, parent, false)
        return CartViewViewHolder(view)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(cartViewViewHolder: CartViewViewHolder, position: Int) {
        val product = data[position]
        cartViewViewHolder.bindData(
            product,
            null, onCartModified, false
        )
    }

    fun setData(data: List<Product>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }
}