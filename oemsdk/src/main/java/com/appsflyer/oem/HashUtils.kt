package com.appsflyer.oem

import org.apache.commons.codec.digest.HmacAlgorithms
import org.apache.commons.codec.digest.HmacUtils

object HashUtils {
    fun hmac(message: String, secretKey: String): String =
        HmacUtils(HmacAlgorithms.HMAC_SHA_256, secretKey).hmacHex(message)
}