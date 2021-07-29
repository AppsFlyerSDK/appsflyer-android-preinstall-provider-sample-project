package com.appsflyer.oem.internal

import org.apache.commons.codec.digest.HmacAlgorithms
import org.apache.commons.codec.digest.HmacUtils

internal object HashUtils {
    fun hmac(message: String, secretKey: String): String =
        HmacUtils(HmacAlgorithms.HMAC_SHA_256, secretKey).hmacHex(message)
}