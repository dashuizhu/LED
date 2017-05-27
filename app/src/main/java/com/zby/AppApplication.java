package com.zby;

import android.app.Application;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

/**
 * Created by zhuj on 2017/5/26 13:04.
 */
public class AppApplication extends Application {

    @Override public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
    }
}
