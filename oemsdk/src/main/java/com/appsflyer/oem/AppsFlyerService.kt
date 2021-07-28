package com.appsflyer.oem

import retrofit2.http.*

interface AppsFlyerService {
    @POST("/v1.0/")
    suspend fun preload(@Header("Authorization") authorization: String,
                        @Body vararg listDataParams: PreInstallInfo): List<PreInstallId>
}