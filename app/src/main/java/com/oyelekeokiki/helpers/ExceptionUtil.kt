package com.oyelekeokiki.helpers

import java.net.UnknownHostException

object ExceptionUtil {
    fun getFetchExceptionMessage(e: Exception): String {
        return if (e is UnknownHostException) {
            NO_INTERNET_CONNECTION
        } else {
            e.localizedMessage ?: UNKNOWN_ERROR
        }
    }
}