package com.oyelekeokiki.ui.wishlist

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.oyelekeokiki.R
import com.oyelekeokiki.helpers.configureCSRecycler
import com.oyelekeokiki.model.Product
import com.oyelekeokiki.ui.shared.CSItemAnimator
import com.oyelekeokiki.ui.shared.ProductAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject

@AndroidEntryPoint
class WishlistFragment : Fragment() {

    @Inject
    lateinit var wishlistViewModel: WishlistViewModel
    lateinit var productAdapter: ProductAdapter

    private var hasAnimationRun = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeWishList()
        observeWishListIds()
        setupSwipeRefreshView()
        initRecyclerView()
    }

    private fun setupSwipeRefreshView() {
        swipe_refresh_layout.isEnabled = false
    }


    private fun initRecyclerView() {
        recycler_home.configureCSRecycler()
        productAdapter = ProductAdapter ({ product, isLiked ->
            wishlistViewModel.updateWishListWithProduct(product, isLiked)
        }, {})
        recycler_home.adapter = productAdapter
    }

    private fun observeWishList() {
        wishlistViewModel.wishlist.observe(
            viewLifecycleOwner,
            Observer { wishList ->
                if (wishList.isEmpty())setEmptyState() else setActiveDataWith(wishList)
            })
    }

    private fun observeWishListIds() {
        wishlistViewModel.wishListProductIds.observe(
            viewLifecycleOwner,
            Observer { ids ->
                productAdapter.setWishListIds(ids)
            })
    }

    private fun setActiveDataWith(products: List<Product>) {
        productAdapter.setData(products)
        text_error_message.visibility = View.GONE
        recycler_home.visibility = View.VISIBLE

        if (!hasAnimationRun) {
            // To prevent recurring animations since this adapter can easily change
            recycler_home.scheduleLayoutAnimation()
            hasAnimationRun = true
        }
    }

    private fun setEmptyState() {
        recycler_home.visibility = View.GONE
        text_error_message.visibility = View.VISIBLE
        text_error_message.text = getString(R.string.wishlist_empty)
    }

}