package com.oyelekeokiki.ui.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.oyelekeokiki.R
import com.oyelekeokiki.helpers.OnCartModified
import com.oyelekeokiki.model.CartItemsToProduct

/**
 * Renders the cart items from the API
 *
 * This class is majorly to implement [CartViewViewHolder] as opposed to conditional statements alone
 * @see [CartViewViewHolder]
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