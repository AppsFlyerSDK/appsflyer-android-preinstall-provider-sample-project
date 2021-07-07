package com.appsflyer

import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.appsflyer.oem.*
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import java.net.URL
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class PreInstallTest {
    companion object {
        const val TIMEOUT = 4L
    }

    @Test
    fun success() {
        val appId = BuildConfig.LIBRARY_PACKAGE_NAME + ".test"
        val preloadId = "AC9FB4FB-AAAA-BBBB-88E6-2840D9BB17F4"
        val server = MockWebServer()
            .also { server ->
                PreInstallEntity(appId, preloadId, "success")
                    .let { listOf(it) }
                    .let { Gson().toJson(it) }
                    .let { MockResponse().setBody(it) }
                    .let { server.enqueue(it) }
            }
        ApiModule.preload = server.url(URL(ApiModule.preload).path).toString()
        val application = ApplicationProvider.getApplicationContext<Application>()
        val mediaSource = "nexus"
        val campaign = "euro2020"
        val installTime = System.currentTimeMillis()
        val campaignId = "final"
        val listDataParams = listOf(DataParams(mediaSource, installTime, appId, campaign, campaignId))
        val bodyExpected = Gson().toJson(listDataParams)
        val callbacks = LinkedBlockingQueue<List<PreInstallEntity>>()
        runBlocking {
            PreInstall(application, { callbacks.offer(it) }, mediaSource).add(listDataParams)
        }
        server
            .takeRequest(TIMEOUT, TimeUnit.SECONDS)
            .let { request ->
                val bodyActual = request.body.readUtf8()
                Assert.assertEquals(bodyExpected, bodyActual)
                val authorizationActual = request.getHeader("Authorization")
                val authorizationExpected = HashUtils.hmac(bodyExpected, mediaSource)
                Assert.assertEquals(authorizationExpected, authorizationActual)
            }
        callbacks.poll(TIMEOUT, TimeUnit.SECONDS)
            .forEach { Assert.assertEquals("success", it.status) }
        callbacks.isEmpty().let { Assert.assertTrue(it) }
        Intent("com.appsflyer.oem.PRELOAD_PROVIDER")
            .let { application.packageManager.queryIntentContentProviders(it, 0) }
            .first()
            .providerInfo
            .authority
            .let { Uri.parse("content://$it/preload_id") }
            .let {
                application.contentResolver.query(
                    it,
                    null,
                    null,
                    null,
                    null
                )
            }!!.let { cursor ->
                cursor.moveToFirst()
                cursor.getString(cursor.getColumnIndex(PreInstallEntity.KEY_PRELOAD_ID))
                    .let { Assert.assertEquals(preloadId, it) }
            }
    }
}