package com.oyelekeokiki.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.oyelekeokiki.R
import com.oyelekeokiki.helpers.configureCSRecycler
import com.oyelekeokiki.helpers.showCSSnackBar
import com.oyelekeokiki.model.Product
import com.oyelekeokiki.ui.shared.ProductAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject

@AndroidEntryPoint
class CartFragment : Fragment() {

    @Inject
    lateinit var cartViewModel: CartViewModel
    lateinit var productAdapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSwipeRefreshView()
        initRecyclerView()

        observeCartItems()
        observeCartItemsFetchError()
        observeSwipeRefresh()

        observeRemoveFromCartSuccess()
        observeRemoveFromCartError()
    }

    private fun setupSwipeRefreshView() {
        context?.let {
            //In order of color softness
            swipe_refresh_layout.setColorSchemeColors(
                ContextCompat.getColor(it, R.color.tertiary_spinner_color),
                ContextCompat.getColor(it, R.color.colorPrimary),
                ContextCompat.getColor(it, R.color.colorPrimaryDark)
            )
        }
        swipe_refresh_layout.setOnRefreshListener {
            cartViewModel.fetchCartItems()
        }
    }


    private fun initRecyclerView() {
        recycler_home.configureCSRecycler()
        productAdapter = ProductAdapter(null, {
            cartViewModel.removeFromCart(it)
        })
        recycler_home.adapter = productAdapter
    }

    private fun observeCartItems() {
        cartViewModel.cartItems.observe(
            viewLifecycleOwner,
            Observer { products ->
                setActiveDataWith(products)
            })
    }

    private fun observeCartItemsFetchError() {
        cartViewModel.errorMessage.observe(
            viewLifecycleOwner,
            Observer { message ->
                setEmptyStateWith(message)
            })
    }

    private fun observeSwipeRefresh() {
        cartViewModel.isFetching.observe(
            viewLifecycleOwner,
            Observer { fetching ->
                setRefreshStateWith(fetching)
            })
    }

    /** Observe and Show Snackbar with Undo action **/
    private fun observeRemoveFromCartSuccess() {
        cartViewModel.removeFromCartSuccess.observe(
            viewLifecycleOwner,
            Observer { (productId, successMessage, type) ->
                cartViewModel.fetchCartItems() // This is not a good approach, implemented because products cannot be queried by ID (on API) or stored on the device
                swipe_refresh_layout.showCSSnackBar(successMessage, type) {
                    cartViewModel.deleteFromCart(productId)
                }
            })
    }

    /** Observe and Show Snackbar with Retry **/
    private fun observeRemoveFromCartError() {
        cartViewModel.deleteFromCartFailed.observe(
            viewLifecycleOwner,
            Observer { (productId, failureReason, type) ->
                swipe_refresh_layout.showCSSnackBar(failureReason, type) {
                    cartViewModel.deleteFromCart(productId)
                }
            })
    }

    private fun setRefreshStateWith(isRefreshing: Boolean) {
        if (isRefreshing) {
            text_error_message.visibility = View.GONE
        }
        swipe_refresh_layout.isRefreshing = isRefreshing
    }

    private fun setActiveDataWith(products: List<Product>) {
        productAdapter.setData(products)
        text_error_message.visibility = View.GONE
        recycler_home.visibility = View.VISIBLE
        recycler_home.scheduleLayoutAnimation()
    }

    private fun setEmptyStateWith(message: String) {
        recycler_home.visibility = View.GONE
        text_error_message.visibility = View.VISIBLE
        text_error_message.text = message
    }
    
}