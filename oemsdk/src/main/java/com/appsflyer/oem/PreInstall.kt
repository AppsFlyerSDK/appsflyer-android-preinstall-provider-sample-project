package com.appsflyer.oem

import android.app.Application
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import java.io.IOException
import kotlin.jvm.Throws

class PreInstall(application: Application, private val mediaSource: String) {
    private val dao = PreInstallDatabase.get(application).referrerDao()
    private val appsFlyerService = ApiModule.appsFlyerService()

    @Throws(IOException::class) // todo should we catch exceptions?
    suspend fun add(listDataParams: List<DataParams>) =
        Gson()
            .toJson(listDataParams)
            .let { HashUtils.hmac(it, mediaSource) }
            .let { appsFlyerService.preload(it, listDataParams) }
            .also { preInstalls ->
                preInstalls.filter { it.status == "success" }
                    .forEach { dao.insert(it) }
            }

    @Throws(IOException::class)
    fun addSync(listDataParams: List<DataParams>) = runBlocking { add(listDataParams) }
}