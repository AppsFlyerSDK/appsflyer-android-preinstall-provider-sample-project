package com.appsflyer.oem.internal

import com.appsflyer.oem.models.PreInstallId
import com.appsflyer.oem.PreInstallInfoRequest
import retrofit2.http.*

interface AppsFlyerService {
    @POST("/v1.0/s2s/download/app/android/{appId}")
    suspend fun registerPreinstalls(@Header("Authorization") authorization: String,
                                    @Path("appId") appId : String,
                                    @Body vararg preInstallInfos: PreInstallInfoRequest
    ): List<PreInstallId>
}