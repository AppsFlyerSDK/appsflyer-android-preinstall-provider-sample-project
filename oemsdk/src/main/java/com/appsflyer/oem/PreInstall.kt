package com.appsflyer.oem

import android.app.Application
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import java.io.IOException

class PreInstall(application: Application, private val mediaSource: String) {
    private val dao = PreInstallDatabase.get(application).referrerDao()
    private val appsFlyerService = ApiModule.appsFlyerService()

    /** be sure to handle IOExceptions */
    @Throws(IOException::class)
    suspend fun add(vararg info: PreInstallInfo) =
        Gson()
            .toJson(info)
            .let { HashUtils.hmac(it, mediaSource) }
            .let { appsFlyerService.preload(it, *info) }
            .also { preInstalls ->
                preInstalls
                    .filter { it.status == "success" }
                    .forEach { dao.insert(it) }
            }

    /** be sure to handle IOExceptions */
    @Throws(IOException::class)
    fun addSync(vararg info: PreInstallInfo) = runBlocking { add(*info) }
}