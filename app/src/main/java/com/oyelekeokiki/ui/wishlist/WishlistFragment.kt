package com.oyelekeokiki.ui.wishlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.oyelekeokiki.R

class WishlistFragment : Fragment() {

    private lateinit var wishlistViewModel: WishlistViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        wishlistViewModel =
            ViewModelProviders.of(this).get(WishlistViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_wishlist, container, false)
        val textView: TextView = root.findViewById(R.id.text_wishlist)
        wishlistViewModel.text.observe(this, Observer {
            textView.text = it
        })
        return root
    }
}