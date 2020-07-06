package com.oyelekeokiki.ui.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.oyelekeokiki.R
import com.oyelekeokiki.utils.OnCartModified
import com.oyelekeokiki.model.CartItemsToProduct

/**
 * Displays the cart items from the API
 */

class CartAdapter(
    private val onCartModified: OnCartModified
) :
    RecyclerView.Adapter<CartViewViewHolder>() {

    private val data: MutableList<CartItemsToProduct> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_list_item_product, parent, false)
        return CartViewViewHolder(view)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(cartViewViewHolder: CartViewViewHolder, position: Int) {
        val cartToProductItem = data[position]
        cartViewViewHolder.bindCartItem(
            cartToProductItem, onCartModified, false
        )
    }

    fun setData(data: List<CartItemsToProduct>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }
}