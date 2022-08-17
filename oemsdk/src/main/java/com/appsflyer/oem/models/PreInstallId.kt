package com.appsflyer.oem.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * This is the internal data base entity used to store received transaction ids from AppFlyer to be later served in the Content provider.
 * This also used as a response model for HTTP call to S2S api.
 */
@Entity
class PreInstallId(
    @PrimaryKey
    @SerializedName(KEY_APP_ID)
    val appId: String,
    @SerializedName(KEY_TRANSACTION_ID)
    val transactionId: String,
    val status: String? = null
) {
    companion object {
        const val KEY_APP_ID = "app_id"
        const val KEY_TRANSACTION_ID = "transaction_id"
    }
}