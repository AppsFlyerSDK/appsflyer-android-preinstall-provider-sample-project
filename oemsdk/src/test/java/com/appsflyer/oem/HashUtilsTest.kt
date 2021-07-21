package com.appsflyer.oem

import com.appsflyer.oem.HashUtils.hmac
import org.junit.Assert
import org.junit.Test

class HashUtilsTest {
    @Test
    fun hmac() {
        val hmac = hmac("pizza", "bitcoin")
        @Suppress("SpellCheckingInspection")
        Assert.assertEquals(
            "368b0a0c673945ce5b87f3afffdff90f8bf41ffff06be1ed394fc54a4458dc94",
            hmac
        )
    }
}