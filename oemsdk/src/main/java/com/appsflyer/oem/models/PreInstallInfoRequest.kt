package com.appsflyer.oem

import android.os.Build
import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * The http request payload data class which is used to send attribution request in S2S flow.
 */
class PreInstallInfoRequest(
    @SerializedName("af_engagement_type")
    val engagementType: EngagementType,
    @SerializedName("af_media_source")
    val mediaSource: String,
    @SerializedName("install_time")
    val installTime: Long,
    @SerializedName("app_id")
    val appId: String,
    @SerializedName("af_campaign")
    val campaignName: String,
    @SerializedName("af_campaign_id")
    val campaignId: String,
    @SerializedName("af_adset")
    val adsetName: String? = null,
    @SerializedName("af_adset_id")
    val adsetId: String? = null,
    @SerializedName("af_ad")
    val adName: String? = null,
    @SerializedName("af_ad_id")
    val adId: String? = null,
    @SerializedName("af_prt")
    val agencyAccountName: String? = null,
    @SerializedName("af_click_id")
    val clickId: String? = null,
    /**
     * Should only be provided when [engagementType] is [EngagementType.CLICK_TO_DOWNLOAD]
     *
     * e.g. 1d - 30d (days) OR 1h-23h (hours).  3 char max
     */
    @SerializedName("af_lookback_window")
    val lookbackWindow: String? = null,
    @SerializedName("af_ad_type")
    val adType: String? = null,
    @SerializedName("af_channel")
    val channel: String? = null,
    @SerializedName("af_cost_model")
    val costModel: String? = null,
    @SerializedName("af_cost_value")
    val costValue: String? = null,
    @SerializedName("af_cost_currency")
    val costCurrency: String? = null,
    @SerializedName("af_custom1")
    val custom1: String? = null,
    @SerializedName("af_custom2")
    val custom2: String? = null,
    @SerializedName("af_custom3")
    val custom3: String? = null,
    @SerializedName("af_custom4")
    val custom4: String? = null,
    @SerializedName("af_custom5")
    val custom5: String? = null,
    @SerializedName("advertising_id")
    val advertisingId: AdvertisingId? = null
) {
    @SerializedName("af_request_id")
    private val requestId = UUID.randomUUID().toString()
    @SerializedName("af_model")
    private val model = Build.MODEL
    @SerializedName("af_os_version")
    private val version = Build.VERSION.RELEASE
}

enum class EngagementType {
    PRELOAD, CLICK_TO_DOWNLOAD
}

class AdvertisingId(
    @SerializedName("af_device_id_type")
    val deviceIdType: DeviceIdType? = null,
    @SerializedName("af_device_id_value")
    val deviceIdValue: String? = null,
)

enum class DeviceIdType {
    AAID, OAID, IMEI, ANDROID_ID, AMAZON_AID
}