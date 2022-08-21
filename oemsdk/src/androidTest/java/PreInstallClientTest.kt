import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.appsflyer.oem.*
import com.appsflyer.oem.internal.ApiModule
import com.appsflyer.oem.internal.HashUtils
import com.appsflyer.oem.models.PreInstallId
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.HttpException
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.TimeUnit

private const val TIMEOUT = 4L

/**
 * This class contains integration tests to verify if sample code properly stores data,
 * received from backend into the room database and then returns it via content provider
 */
@RunWith(AndroidJUnit4::class)
class PreInstallClientTest {

    private val appId = BuildConfig.LIBRARY_PACKAGE_NAME + ".test"
    private val preloadId = "AC9FB4FB-AAAA-BBBB-88E6-2840D9BB17F4"

    // mock response from S2S api
    private val server = MockWebServer()

    @Before
    fun setUp() {
        // make api module send requests to mockweb server
        ApiModule.preloadUrl = server.url(URL(ApiModule.preloadUrl).path).toString()
    }

    @Test
    fun success() {
        mockS2SResponse(HttpURLConnection.HTTP_OK)

        val application = ApplicationProvider.getApplicationContext<Application>()
        val mediaSource = "nexus"
        val campaign = "euro2020"
        val installTime = System.currentTimeMillis()
        val campaignId = "final"
        val info = PreInstallInfoRequest(
            engagementType = EngagementType.PRELOAD,
            mediaSource = mediaSource,
            installTime = installTime,
            appId = appId,
            campaignName = campaign,
            campaignId = campaignId,
        )
        val bodyExpected = info.let(::listOf).let(Gson()::toJson)
        runBlocking { PreInstallClient(application, mediaSource).attributeAppsInstall(info) }
            .forEach { Assert.assertEquals("success", it.status) }
        server
            .takeRequest(TIMEOUT, TimeUnit.SECONDS)
            .let { request ->
                val bodyActual = request!!.body.readUtf8()
                Assert.assertEquals(bodyExpected, bodyActual)
                val authorizationActual = request.getHeader("Authorization")
                val authorizationExpected = HashUtils.hmac(bodyExpected, mediaSource)
                // verify if we properly sent auth
                Assert.assertEquals(authorizationExpected, authorizationActual)
            }

        Intent("com.appsflyer.referrer.INSTALL_PROVIDER")
            .let { application.packageManager.queryIntentContentProviders(it, 0) }
            .first()
            .providerInfo
            .authority
            .let { Uri.parse("content://$it/transaction_id") }
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
                cursor.getString(cursor.getColumnIndex(PreInstallId.KEY_TRANSACTION_ID))
                    .let { Assert.assertEquals(preloadId, it) }
            }
    }

    @Test(expected = HttpException::class)
    fun httpNot200Ok() {
        mockS2SResponse(HttpURLConnection.HTTP_BAD_GATEWAY)

        ApiModule.preloadUrl = server.url(URL(ApiModule.preloadUrl).path).toString()
        val application = ApplicationProvider.getApplicationContext<Application>()
        val mediaSource = "nexus"
        val campaign = "euro2020"
        val installTime = System.currentTimeMillis()
        val campaignId = "final"
        val info = PreInstallInfoRequest(
            engagementType = EngagementType.PRELOAD,
            mediaSource = mediaSource,
            installTime = installTime,
            appId = appId,
            campaignName = campaign,
            campaignId = campaignId
        )
        runBlocking { PreInstallClient(application, mediaSource).attributeAppsInstall(info) }
            .forEach { Assert.assertEquals("success", it.status) }
    }

    private fun mockS2SResponse(statusCode: Int) {
        val body = listOf(PreInstallId(appId, preloadId, "success"))
        val jsonBody = Gson().toJson(body)
        val mockResponse = MockResponse()
            .setResponseCode(statusCode)
            .setBody(jsonBody)

        server.enqueue(mockResponse)
    }
}