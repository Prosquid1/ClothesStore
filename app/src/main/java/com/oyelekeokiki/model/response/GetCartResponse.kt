package com.oyelekeokiki.model.response

import com.oyelekeokiki.model.CartItem

data class GetCartResponse(val cartItems: List<CartItem> = listOf())