import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.appsflyer.oem.BuildConfig
import com.appsflyer.oem.EngagementType
import com.appsflyer.oem.PreInstallClient
import com.appsflyer.oem.PreInstallInfoRequest
import com.appsflyer.oem.internal.ApiModule
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

private const val EXPECTED_DEV_KEY = "replace with your AppsFlyer S2S dev key"
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
        val bodyExpected = Gson().toJson(info)
        val response =
            runBlocking { PreInstallClient(application, mediaSource).registerAppInstall(info) }
        Assert.assertEquals(response.transactionId, preloadId)

        server
            .takeRequest(TIMEOUT, TimeUnit.SECONDS)
            .let { request ->
                val bodyActual = request!!.body.readUtf8()
                Assert.assertEquals(bodyExpected, bodyActual)
                val authorizationActual = request.getHeader("Authorization")
                // verify if we properly sent auth
                Assert.assertEquals(EXPECTED_DEV_KEY, authorizationActual)
            }
        try {
            Intent("com.appsflyer.referrer.INSTALL_PROVIDER")
                .let { application.packageManager.queryIntentContentProviders(it, 0) }
                .firstOrNull()
                ?.providerInfo
                ?.authority
                ?.let { Uri.parse("content://$it/transaction_id") }
                ?.let { contentUri ->
                    val contentProviderClient = application.contentResolver.acquireUnstableContentProviderClient(contentUri)
                    contentProviderClient.use { client ->
                        val cursor = client?.query(contentUri, null, null, null, null)
                        cursor?.use { cursor ->
                            if (cursor.moveToFirst()) {
                                val transactionIdColumnIndex = cursor.getColumnIndex(PreInstallId.KEY_TRANSACTION_ID)
                                val transactionId = cursor.getString(transactionIdColumnIndex)
                                Assert.assertEquals(preloadId, transactionId)
                            } else {
                                // Handle case where the cursor is empty
                            }
                        }
                    }
                }
        } catch (e: Exception) {
            // Handle exceptions here
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
        runBlocking { PreInstallClient(application, mediaSource).registerAppInstall(info) }
    }

    private fun mockS2SResponse(statusCode: Int) {
        val body = PreInstallId(appId, preloadId)
        val jsonBody = Gson().toJson(body)
        val mockResponse = MockResponse()
            .setResponseCode(statusCode)
            .setBody(jsonBody)

        server.enqueue(mockResponse)
    }
}