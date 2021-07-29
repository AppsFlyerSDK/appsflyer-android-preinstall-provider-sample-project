package com.appsflyer.oem.internal

import com.appsflyer.oem.PreInstallId
import com.appsflyer.oem.PreInstallInfo
import retrofit2.http.*

interface AppsFlyerService {
    @POST("/v1.0/")
    suspend fun preload(@Header("Authorization") authorization: String,
                        @Body vararg listDataParams: PreInstallInfo
    ): List<PreInstallId>
}