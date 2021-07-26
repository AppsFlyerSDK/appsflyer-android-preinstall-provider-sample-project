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
    suspend fun add(listDataParams: List<DataParams>) =
        Gson()
            .toJson(listDataParams)
            .let { HashUtils.hmac(it, mediaSource) }
            .let { appsFlyerService.preload(it, listDataParams) }
            .also { preInstalls ->
                preInstalls
                    .filter { it.status == "success" }
                    .forEach { dao.insert(it) }
            }

    /** be sure to handle IOExceptions */
    @Throws(IOException::class)
    fun addSync(listDataParams: List<DataParams>) = runBlocking { add(listDataParams) }
}