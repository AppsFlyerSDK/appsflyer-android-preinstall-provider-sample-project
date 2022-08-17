import android.app.Application;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.appsflyer.oem.EngagementType;
import com.appsflyer.oem.internal.ApiModule;
import com.appsflyer.oem.BuildConfig;
import com.appsflyer.oem.PreInstallInfo;
import com.appsflyer.oem.PreInstallClient;
import com.appsflyer.oem.PreInstallIdEntity;
import com.google.gson.Gson;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

@RunWith(AndroidJUnit4.class)
public class PreInstallClientJavaInteropTest {
    @Test
    public void compatibility() throws IOException {
        Application application = ApplicationProvider.getApplicationContext();
        String appId = BuildConfig.LIBRARY_PACKAGE_NAME + ".test";
        String preloadId = "AC9FB4FB-AAAA-BBBB-88E6-2840D9BB17F4";
        PreInstallIdEntity entity = new PreInstallIdEntity(appId, preloadId, "success");
        List<PreInstallIdEntity> preInstallsExpected = Collections.singletonList(entity);
        String json = new Gson().toJson(preInstallsExpected);
        MockWebServer server = new MockWebServer();
        server.enqueue(new MockResponse().setBody(json));
        String preloadLocal = server
                .url(new URL(ApiModule.INSTANCE.getPreloadUrl()).getPath())
                .toString();
        ApiModule.INSTANCE.setPreloadUrl(preloadLocal);
        String mediaSource = "nexus";
        PreInstallInfo preInstallInfo = new PreInstallInfo(
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
        List<PreInstallIdEntity> preInstallsActual = preInstallClient.addSync(preInstallInfo);
        Assert.assertEquals(preInstallsExpected.get(0).getTransactionId(),
                preInstallsActual.get(0).getTransactionId());
    }

    @Test(expected = IOException.class)
    public void network() throws IOException {
        Application application = ApplicationProvider.getApplicationContext();
        String appId = BuildConfig.LIBRARY_PACKAGE_NAME + ".test";
        String mediaSource = "nexus";
        PreInstallInfo preInstallInfo = new PreInstallInfo(EngagementType.PRELOAD, mediaSource,
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
        preInstallClient.addSync(preInstallInfo);
    }
}