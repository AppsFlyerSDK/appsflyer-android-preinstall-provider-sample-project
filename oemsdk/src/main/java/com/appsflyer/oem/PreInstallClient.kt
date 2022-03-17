package com.appsflyer.oem

import android.app.Application
import com.appsflyer.oem.internal.ApiModule
import com.appsflyer.oem.internal.HashUtils
import com.appsflyer.oem.internal.PreInstallDatabase
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException
import java.io.IOException

class PreInstallClient(application: Application, private val mediaSource: String) {
    private val dao = PreInstallDatabase.get(application).preInstallDao()
    private val appsFlyerService = ApiModule.appsFlyerService()

    /** be sure to handle Exceptions */
    @Throws(IOException::class, HttpException::class)
    suspend fun add(vararg infos: PreInstallInfo) =
        Gson()
            .toJson(infos)
            .let { HashUtils.hmac(it, mediaSource) }
            .let { authToken ->
                val infosByAppId = infos.groupBy { it.appId }
                val appIds = infosByAppId.keys
                for (appId in appIds) {
                    infosByAppId[appId]?.let { infosForAppId ->
                        appsFlyerService.registerPreinstalls(
                            authorization = authToken,
                            appId = appId,
                            preInstallInfos = infosForAppId.toTypedArray()
                        ).also { preInstallIds ->
                            preInstallIds
                                .filter { it.status == "success" }
                                .forEach { dao.insert(it) }
                        }
                    }
                }
            }

    /** be sure to handle Exceptions */
    @Throws(IOException::class, HttpException::class)
    fun addSync(vararg info: PreInstallInfo) = runBlocking { add(*info) }
}