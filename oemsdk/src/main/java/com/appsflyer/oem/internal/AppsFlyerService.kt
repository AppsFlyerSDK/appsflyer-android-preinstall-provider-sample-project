package com.appsflyer.oem.internal

import com.appsflyer.oem.PreInstallId
import com.appsflyer.oem.PreInstallInfo
import retrofit2.http.*

interface AppsFlyerService {
    @POST("/v1.0/")
    suspend fun registerPreinstalls(@Header("Authorization") authorization: String,
                                    @Body vararg preInstallInfos: PreInstallInfo
    ): List<PreInstallId>
}