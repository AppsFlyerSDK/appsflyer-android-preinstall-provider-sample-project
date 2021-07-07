package com.appsflyer.oem

import com.google.gson.annotations.SerializedName

class DataParams(
    @SerializedName("pid")
    val mediaSource: String,
    @SerializedName("install_time")
    val installTime: Long,
    @SerializedName("app_id")
    val appId: String,
    @SerializedName("c")
    val campaign: String,
    @SerializedName("af_c_id")
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
    @SerializedName("network_tran_id")
    val adNetworkUniqueTransactionIdentifier: String? = null,
    @SerializedName("af_ad_type")
    val adType: String? = null,
    @SerializedName("af_channel")
    val mediaSourceChannel: String? = null,
    @SerializedName("af_cost_model")
    val costModel: String? = null,
    @SerializedName("af_cost_value")
    val costValue: String? = null,
    @SerializedName("af_cost_currency")
    val costCurrency: String? = null,
    @SerializedName("af_sub1")
    val custom1: String? = null,
    @SerializedName("af_sub2")
    val custom2: String? = null,
    @SerializedName("af_sub3")
    val custom3: String? = null,
    @SerializedName("af_sub4")
    val custom4: String? = null,
    @SerializedName("af_sub5")
    val custom5: String? = null
)