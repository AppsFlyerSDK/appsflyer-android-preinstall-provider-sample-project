package com.appsflyer.oem;

import org.jetbrains.annotations.NotNull;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class HashUtils {
    @NotNull
    private static String bytesToHex(@NotNull byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte byt : bytes)
            result.append(Integer.toString((byt & 0xff) + 0x100, 16).substring(1));
        return result.toString();
    }

    public static String hmac(@NotNull String message, @NotNull String secretKey) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secretKey.getBytes(), "HmacSHA256"));
            byte[] hmacSha256 = mac.doFinal(message.getBytes());
            return bytesToHex(hmacSha256).toLowerCase();
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            return e.getMessage();
        }
    }
}