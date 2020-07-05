package com.oyelekeokiki.helpers

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.oyelekeokiki.R
import com.oyelekeokiki.model.CartItem
import com.oyelekeokiki.model.CartToProductItem
import com.oyelekeokiki.model.Product
import com.oyelekeokiki.ui.shared.CSItemAnimator
import java.util.*

const val NO_INTERNET_CONNECTION = "No Internet connection!"

fun String.formatPrice(): String {
    return "£${this}"
}

fun String.convertToNCharacters(n: Int, padChar: Char = '.'): String {
    if (this.length >= n) {
        return this.take(n)
    }
    return this.padEnd(n - this.length, padChar)
}

fun RecyclerView.configureCSRecycler() {
    val itemDecoration = DividerItemDecoration(
        context,
        DividerItemDecoration.VERTICAL
    )
    context?.let {
        itemDecoration.setDrawable(
            ColorDrawable(
                ContextCompat.getColor(
                    it,
                    R.color.lightcolorPrimary
                )
            )
        )
    }
    addItemDecoration(
        itemDecoration
    )
    itemAnimator = CSItemAnimator()
    layoutManager = LinearLayoutManager(context)
}

fun List<Product>.getProductsInIDsList(productIds: List<Int>): List<Product> {
    return productIds.flatMap { mappedId -> this.filter { mappedId == it.id } }
}

fun List<CartToProductItem>.getTotalValue(): String {
    return this.toList()
        .sumBy { ((it.product.price ?: "").toDouble().toInt() * it.cartItemIds.size) }.toString()
}

fun List<Product>.convertToCartProduct(cartItemIds: List<CartItem>): List<CartToProductItem> {
    val cartItemsGroupedByProduct = cartItemIds.groupBy { it.productId }.values

    val productIdToProductMap = this.map { Pair(it.id, it) }.toMap()
    val productIdToCartItemsMap =
        cartItemsGroupedByProduct.map { array -> Pair(array[0].productId, array.map { it.id }) }
            .toMap()

    return productIdToCartItemsMap.map {
        CartToProductItem(
            productIdToProductMap[it.key] as Product,
            productIdToCartItemsMap[it.key] as List<Int>
        )
    }
}

fun ViewGroup.showCSSnackBar(
    message: String,
    responseWithType: ActionResponseType? = null,
    action: ((View) -> Unit)? = null
) {
    val snackBarLength =
        if (responseWithType == ActionResponseType.SUCCESS) Snackbar.LENGTH_SHORT else Snackbar.LENGTH_LONG

    val snackbarActionColorId =
        if (responseWithType == ActionResponseType.SUCCESS) R.color.green else R.color.red

    val snackBar = Snackbar
        .make(this, message, snackBarLength)
    action.let {
        val snackbarActionMessage =
            if (responseWithType == ActionResponseType.SUCCESS) this.context.getString(R.string.undo) else this.context.getString(
                R.string.retry
            )
        if (snackbarActionMessage.isEmpty()) return@let
        snackBar.setAction(snackbarActionMessage, it)
        snackBar.setActionTextColor(ContextCompat.getColor(context, snackbarActionColorId))
    }

    snackBar.show()
}

object ColorHelper {

    private fun getModifiedHexInt(char: Char): Int {
        val charInt = char.toInt()
        val randomizingSeed = if (charInt % 2 == 0) 1 else 2
        return charInt * randomizingSeed
    }

    fun generateColorFromText(string: String): Int {
        val mutatedString = string.toLowerCase(Locale.ROOT).convertToNCharacters(3)

        //Randomizing the colors since hex characters from strings are so close, they produce almost the same hue
        val colorArray = mutatedString.map { getModifiedHexInt(it) }

        // Randomizing again to spark up the colors!...
        val red = colorArray[0] + colorArray[1]
        val green = colorArray[1] + colorArray[2]
        val blue = colorArray[2] + colorArray[0]

        return Color.rgb(red, green, blue)
    }
}

enum class StockCountPriority {
    LOW, MEDIUM;

    fun getColor() = when (this) {
        LOW -> R.color.red
        MEDIUM -> R.color.money_blue
    }
}

enum class ActionResponseType() {
    SUCCESS, ERROR;
}


