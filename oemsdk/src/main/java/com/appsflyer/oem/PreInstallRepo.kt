package com.appsflyer.oem

import android.content.Context

class PreInstallRepo(private val referrerDao: PreInstallDao) {
    companion object {
        fun get(context: Context) = PreInstallRepo(PreInstallDatabase.get(context).referrerDao())
    }

    fun select(appId: String) = referrerDao.select(appId)
    suspend fun insert(referrer: PreInstallEntity) = referrerDao.insert(referrer)
}