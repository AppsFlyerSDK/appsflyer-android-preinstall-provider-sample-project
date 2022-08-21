import android.app.Application;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.appsflyer.oem.BuildConfig;
import com.appsflyer.oem.DownloadRegisterFailedException;
import com.appsflyer.oem.EngagementType;
import com.appsflyer.oem.PreInstallClient;
import com.appsflyer.oem.PreInstallInfoRequest;
import com.appsflyer.oem.internal.ApiModule;
import com.appsflyer.oem.models.PreInstallId;
import com.google.gson.Gson;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.net.URL;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

/**
 * Test verifies if the preinstall code can be called by java clients
 */
@RunWith(AndroidJUnit4.class)
public class PreInstallClientJavaInteropTest {
    @Test
    public void dataIsSavedOnSuccessResponse() throws IOException, DownloadRegisterFailedException {
        Application application = ApplicationProvider.getApplicationContext();
        String appId = BuildConfig.LIBRARY_PACKAGE_NAME + ".test";
        String preloadId = "AC9FB4FB-AAAA-BBBB-88E6-2840D9BB17F4";
        PreInstallId preInstallExpected = new PreInstallId(appId, preloadId, "success");
        String json = new Gson().toJson(preInstallExpected);

        MockWebServer server = new MockWebServer();
        server.enqueue(new MockResponse().setBody(json));
        String preloadLocal = server
                .url(new URL(ApiModule.INSTANCE.getPreloadUrl()).getPath())
                .toString();
        ApiModule.INSTANCE.setPreloadUrl(preloadLocal);
        String mediaSource = "nexus";
        PreInstallInfoRequest preInstallInfo = new PreInstallInfoRequest(
                EngagementType.PRELOAD,
                mediaSource,
                System.currentTimeMillis(),
                appId,
                "euro2020",
                "final",
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null);
        PreInstallClient preInstallClient = new PreInstallClient(application, mediaSource);
        PreInstallId preInstallActual = preInstallClient.registerAppInstallSync(preInstallInfo);

        Assert.assertEquals(preInstallExpected.getTransactionId(),
                preInstallActual.getTransactionId());
    }

}