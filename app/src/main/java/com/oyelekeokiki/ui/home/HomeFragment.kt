package com.oyelekeokiki.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.oyelekeokiki.R
import com.oyelekeokiki.helpers.ActionResponseType
import com.oyelekeokiki.helpers.configureCSRecycler
import com.oyelekeokiki.model.Product
import com.oyelekeokiki.ui.shared.ProductAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject


@AndroidEntryPoint
class HomeFragment : Fragment() {

    @Inject
    lateinit var homeViewModel: HomeViewModel
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

        observeProducts()
        observeProductsFetchError()
        observeWishList()
        observeSwipeRefresh()

        observeAddToCartSuccess()
        observeAddToCartError()
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
            homeViewModel.fetchProducts()
        }
    }


    private fun initRecyclerView() {
        recycler_home.configureCSRecycler()
        productAdapter = ProductAdapter({ product, isLiked ->
            homeViewModel.updateWishListWithProduct(product, isLiked)
        }, {
            homeViewModel.addToCart(it)
        })
        recycler_home.adapter = productAdapter
    }

    private fun observeProducts() {
        homeViewModel.products.observe(
            viewLifecycleOwner,
            Observer { products ->
                setActiveDataWith(products)
            })
    }

    private fun observeWishList() {
        homeViewModel.wishListProductIds.observe(
            viewLifecycleOwner,
            Observer { ids ->
                productAdapter.setWishListIds(ids)
            })
    }

    private fun observeProductsFetchError() {
        homeViewModel.errorMessage.observe(
            viewLifecycleOwner,
            Observer { message ->
                setEmptyStateWith(message)
            })
    }

    private fun observeSwipeRefresh() {
        homeViewModel.isFetching.observe(
            viewLifecycleOwner,
            Observer { fetching ->
                setRefreshStateWith(fetching)
            })
    }

    /** Observe and Show Snackbar with Undo action **/
    private fun observeAddToCartSuccess() {
        homeViewModel.addToCartSuccess.observe(
            viewLifecycleOwner,
            Observer { (productId, successMessage, type) ->
                homeViewModel.fetchProducts() // This is not a good approach, implemented because products cannot be queried by ID (on API) or stored on the device
                showSnackBarWithAction(successMessage, type) {
                    homeViewModel.deleteFromCart(productId)
                }
            })
    }

    /** Observe and Show Snackbar with Retry **/
    private fun observeAddToCartError() {
        homeViewModel.addToCartFailed.observe(
            viewLifecycleOwner,
            Observer { (productId, failureReason, type) ->
                showSnackBarWithAction(failureReason, type) {
                    homeViewModel.addToCart(productId)
                }
            })
    }

    private fun showSnackBarWithAction(
        message: String,
        responseWithType: ActionResponseType,
        action: (View) -> Unit
    ) {
        val snackBarLength =
            if (responseWithType == ActionResponseType.SUCCESS) Snackbar.LENGTH_SHORT else Snackbar.LENGTH_LONG
        val snackbarActionMessage =
            if (responseWithType == ActionResponseType.SUCCESS) getString(R.string.undo) else getString(
                R.string.retry
            )
        val snackbarActionColorId =
            if (responseWithType == ActionResponseType.SUCCESS) R.color.green else R.color.red

        val snackBar = Snackbar
            .make(swipe_refresh_layout, message, snackBarLength)
        snackBar.setAction(snackbarActionMessage, action)
        context?.let {
            snackBar.setActionTextColor(ContextCompat.getColor(it, snackbarActionColorId))
        }

        snackBar.show()
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