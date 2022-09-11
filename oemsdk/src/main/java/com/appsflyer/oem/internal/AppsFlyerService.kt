package com.appsflyer.oem.internal

import com.appsflyer.oem.PreInstallInfoRequest
import com.appsflyer.oem.models.PreInstallId
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface AppsFlyerService {

    /**
     * S2S endpoint to register an app was downloaded event.
     * @return [PreInstallId] object containing transaction_id which should be later shared via ContentProvider
     */
    @POST("/v1.0/s2s/download/app/android/{appId}")
    suspend fun registerAppDownload(
        @Header("Authorization") authorization: String,
        @Path("appId") appId: String,
        @Body preInstallInfo: PreInstallInfoRequest
    ): PreInstallId
}