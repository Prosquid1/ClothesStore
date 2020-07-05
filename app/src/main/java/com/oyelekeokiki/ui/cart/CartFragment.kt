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
import com.oyelekeokiki.model.CartToProductItem
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject

@AndroidEntryPoint
class CartFragment : Fragment() {

    @Inject
    lateinit var cartViewModel: CartViewModel
    lateinit var cartAdapter: CartAdapter

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

        observeReAddedToCartSuccess()
        observeReAddedToCartError()

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
        cartAdapter = CartAdapter {
            cartViewModel.deleteFromCart(it)
        }
        recycler_home.adapter = cartAdapter
    }

    private fun observeCartItems() {
        cartViewModel.cartItems.observe(
            viewLifecycleOwner,
            Observer { cartToProductItems ->
                setActiveDataWith(cartToProductItems)
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
    private fun observeReAddedToCartSuccess() {
        cartViewModel.cartItemAddedSuccess.observe(
            viewLifecycleOwner,
            Observer { (_, successMessage, _) ->
                swipe_refresh_layout.showCSSnackBar(successMessage)
            })
    }

    /** Observe and Show Snackbar with Retry **/
    private fun observeReAddedToCartError() {
        cartViewModel.cartItemAddedFailed.observe(
            viewLifecycleOwner,
            Observer { (cartItem, failureReason, type) ->
                swipe_refresh_layout.showCSSnackBar(failureReason, type) {
                    cartViewModel.addToCart(cartItem)
                }
            })
    }

    /** Observe and Show Snackbar with Undo action **/
    private fun observeRemoveFromCartSuccess() {
        cartViewModel.cartItemDeletedSuccess.observe(
            viewLifecycleOwner,
            Observer { (cartItem, successMessage, type) ->
                swipe_refresh_layout.showCSSnackBar(successMessage, type) {
                    cartViewModel.addToCart(cartItem)
                }
            })
    }

    /** Observe and Show Snackbar with Retry **/
    private fun observeRemoveFromCartError() {
        cartViewModel.cartItemDeletedFailed.observe(
            viewLifecycleOwner,
            Observer { (cartItem, failureReason, type) ->
                swipe_refresh_layout.showCSSnackBar(failureReason, type) {
                    cartViewModel.deleteFromCart(cartItem)
                }
            })
    }

    private fun setRefreshStateWith(isRefreshing: Boolean) {
        if (isRefreshing) {
            text_error_message.visibility = View.GONE
        }
        swipe_refresh_layout.isRefreshing = isRefreshing
    }

    private fun setActiveDataWith(cartToProductItems: List<CartToProductItem>) {
        cartAdapter.setData(cartToProductItems)
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