package com.oyelekeokiki.helpers

fun String.formatPrice(): String {
    return "£${this}"
}

fun String.convertToNCharacters(n: Int, padChar: Char = '.'): String {
    if (this.length >= n) {
        return this.take(n)
    }
    return this.padEnd(n - this.length, padChar)
}