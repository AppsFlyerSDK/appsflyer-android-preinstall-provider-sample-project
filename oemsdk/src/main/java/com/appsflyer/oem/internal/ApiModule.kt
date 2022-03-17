package com.appsflyer.oem.internal

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiModule {
    var preloadUrl = "https://engagements.appsflyer.com/"
    fun appsFlyerService(): AppsFlyerService = Retrofit.Builder()
        .baseUrl(preloadUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(AppsFlyerService::class.java)
}