package com.appsflyer.oem.internal

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiModule {
    var preload = "https://preload.appsflyer.com/"
    fun appsFlyerService(): AppsFlyerService = Retrofit.Builder()
        .baseUrl(preload)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(AppsFlyerService::class.java)
}