package com.appsflyer.oem

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * This is the internal data base entity used to store received transaction ids from AppFlyer to be later served in the Content provider.
 */
@Entity
class PreInstallIdEntity(
    @PrimaryKey
    @SerializedName(KEY_APP_ID)
    val appId: String,
    @SerializedName(KEY_TRANSACTION_ID)
    val transactionId: String,
    val status: String?
) {
    companion object {
        const val KEY_APP_ID = "app_id"
        const val KEY_TRANSACTION_ID = "transaction_id"
    }

    constructor(appId: String, preloadId: String) : this(appId, preloadId, null)
}