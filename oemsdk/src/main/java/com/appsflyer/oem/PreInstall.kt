package com.appsflyer.oem

import android.app.Application
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking

class PreInstall(
    application: Application,
    private val result: ((List<PreInstallEntity>) -> Unit)? = null,
    private val mediaSource: String
) {
    private val dao = PreInstallDatabase.get(application).referrerDao()
    private val appsFlyerService = ApiModule.appsFlyerService()

    suspend fun add(listDataParams: List<DataParams>) {
        Gson()
            .toJson(listDataParams)
            .let { HashUtils.hmac(it, mediaSource) }
            .let { appsFlyerService.preload(it, listDataParams) }
            .also { result?.invoke(it) }
            .filter { it.status == "success" }
            .forEach { dao.insert(it) }
    }

    fun addSync(listDataParams: List<DataParams>) = runBlocking { add(listDataParams) }
}