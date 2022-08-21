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

class DownloadRegisterFailedException(val s2sResponse: PreInstallId) : Exception()

class PreInstallClient(application: Application, private val mediaSource: String) {
    private val dao = PreInstallDatabase.get(application).preInstallDao()
    private val appsFlyerService = ApiModule.appsFlyerService()

    /**
     * Performs api call to S2S endpoint and saves successful response to the [PreInstallDatabase]
     *
     * @return fetched and successfully saved [PreInstallId]
     * @throws [IOException], [HttpException] in case of network errors
     * @throws [DownloadRegisterFailedException] in case we received fail response from S2S endpoint
     * */
    @Throws(IOException::class, HttpException::class, DownloadRegisterFailedException::class)
    suspend fun registerAppInstall(info: PreInstallInfoRequest): PreInstallId {
        // call S2S endpoint with app download attribution data
        val response = appsFlyerService.registerAppDownload(
            authorization = DEV_KEY,
            appId = info.appId,
            preInstallInfo = info
        )

        // on success response - save response to the local database, which will be later queried by content provider
        if (response.status == "success") {
            dao.insert(response)
        } else {
            throw DownloadRegisterFailedException(response)
        }

        return response
    }


    /**
     * Synchronous alias for [PreInstallClient.registerAppInstall] main usecase is call api from java code.
     */
    @Throws(IOException::class, HttpException::class, DownloadRegisterFailedException::class)
    fun registerAppInstallSync(info: PreInstallInfoRequest) =
        runBlocking { registerAppInstall(info) }
}