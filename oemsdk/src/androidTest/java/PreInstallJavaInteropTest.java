import android.app.Application;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.appsflyer.oem.ApiModule;
import com.appsflyer.oem.BuildConfig;
import com.appsflyer.oem.DataParams;
import com.appsflyer.oem.PreInstall;
import com.appsflyer.oem.PreInstallEntity;
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
public class PreInstallJavaInteropTest {
    @Test
    public void compatibility() throws IOException {
        Application application = ApplicationProvider.getApplicationContext();
        String appId = BuildConfig.LIBRARY_PACKAGE_NAME + ".test";
        String preloadId = "AC9FB4FB-AAAA-BBBB-88E6-2840D9BB17F4";
        PreInstallEntity entity = new PreInstallEntity(appId, preloadId, "success");
        List<PreInstallEntity> preInstallsExpected = Collections.singletonList(entity);
        String json = new Gson().toJson(preInstallsExpected);
        MockWebServer server = new MockWebServer();
        server.enqueue(new MockResponse().setBody(json));
        String preloadLocal = server
                .url(new URL(ApiModule.INSTANCE.getPreload()).getPath())
                .toString();
        ApiModule.INSTANCE.setPreload(preloadLocal);
        String mediaSource = "nexus";
        DataParams dataParams = new DataParams(mediaSource,
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
                null);
        List<DataParams> dataParamsList = Collections.singletonList(dataParams);
        PreInstall preInstall = new PreInstall(application, mediaSource);
        List<PreInstallEntity> preInstallsActual = preInstall.addSync(dataParamsList);
        Assert.assertEquals(preInstallsExpected.get(0).getPreloadId(),
                preInstallsActual.get(0).getPreloadId());
    }

    @Test(expected = IOException.class)
    public void network() throws IOException {
        Application application = ApplicationProvider.getApplicationContext();
        String appId = BuildConfig.LIBRARY_PACKAGE_NAME + ".test";
        String mediaSource = "nexus";
        DataParams dataParams = new DataParams(mediaSource,
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
                null);
        List<DataParams> dataParamsList = Collections.singletonList(dataParams);
        PreInstall preInstall = new PreInstall(application, mediaSource);
        preInstall.addSync(dataParamsList);
    }
}