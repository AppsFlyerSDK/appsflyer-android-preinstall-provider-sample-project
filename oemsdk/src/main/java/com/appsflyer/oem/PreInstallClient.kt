package com.appsflyer.oem

import android.app.Application
import com.appsflyer.oem.internal.ApiModule
import com.appsflyer.oem.internal.PreInstallDatabase
import com.appsflyer.oem.models.PreInstallId
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException
import java.io.IOException

/**
 * Used for authorization with appsflyer servers
 */
private const val DEV_KEY = "replace with your AppsFlyer S2S dev key"

class PreInstallClient(application: Application, private val mediaSource: String) {
    private val dao = PreInstallDatabase.get(application).preInstallDao()
    private val appsFlyerService = ApiModule.appsFlyerService()

    /**
     * Performs api call to S2S endpoint and saves successful response to the [PreInstallDatabase]
     *
     * @return fetched and successfully saved [PreInstallId]
     * @throws [IOException] in case of network errors
     * @throws [HttpException] in case of fail response from backend
     * (see "AppsFlyer Preload Campaign Measurement" doc for details)
     * */
    @Throws(IOException::class, HttpException::class)
    suspend fun registerAppInstall(info: PreInstallInfoRequest): PreInstallId {
        // call S2S endpoint with app download attribution data
        val response = appsFlyerService.registerAppDownload(
            authorization = DEV_KEY,
            appId = info.appId,
            preInstallInfo = info
        )

        // on success response - save response to the local database, which will be later queried by content provider
        dao.insert(response)

        return response
    }


    /**
     * Synchronous alias for [PreInstallClient.registerAppInstall] main usecase is call api from java code.
     */
    @Throws(IOException::class, HttpException::class)
    fun registerAppInstallSync(info: PreInstallInfoRequest) =
        runBlocking { registerAppInstall(info) }
}