package com.oyelekeokiki.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.oyelekeokiki.R
import com.oyelekeokiki.utils.configureCSRecycler
import com.oyelekeokiki.utils.showCSSnackBar
import com.oyelekeokiki.model.CartItemsToProduct
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_my_cart.*
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
        return inflater.inflate(R.layout.fragment_my_cart, container, false)
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

        observeTotalValueText()
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

    private fun observeTotalValueText() {
        cartViewModel.totalValueText.observe(
            viewLifecycleOwner,
            Observer {
                total_value_text.text = it
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

    private fun setActiveDataWith(cartItemsToProduct: List<CartItemsToProduct>) {
        cartAdapter.setData(cartItemsToProduct)
        text_error_message.visibility = View.GONE
        recycler_home.visibility = View.VISIBLE
        total_text_layout.visibility = View.VISIBLE
        payment_button.visibility = View.VISIBLE
        recycler_home.scheduleLayoutAnimation()
    }

    private fun setEmptyStateWith(message: String) {
        recycler_home.visibility = View.GONE
        text_error_message.visibility = View.VISIBLE
        text_error_message.text = message
        total_text_layout.visibility = View.GONE
        payment_button.visibility = View.GONE
    }

}