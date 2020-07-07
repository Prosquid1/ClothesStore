package com.oyelekeokiki

import com.oyelekeokiki.helpers.ExceptionUtil
import com.oyelekeokiki.helpers.NO_INTERNET_CONNECTION
import org.junit.Test
import java.net.UnknownHostException

class ExceptionUtilTest {
    @Test
    fun castUnknownHostErrorToNoInternetString() {
        assert(
            ExceptionUtil.getFetchExceptionMessage(
                UnknownHostException("Any message should be casted to Internet Exception")
            ) == NO_INTERNET_CONNECTION
        )
    }
}