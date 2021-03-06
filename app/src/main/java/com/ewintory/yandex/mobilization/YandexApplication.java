package com.ewintory.yandex.mobilization;

import android.app.Application;
import android.content.Context;

import com.ewintory.yandex.mobilization.network.DaggerNetworkComponent;
import com.ewintory.yandex.mobilization.network.NetworkComponent;
import com.ewintory.yandex.mobilization.network.NetworkModule;
import com.squareup.leakcanary.RefWatcher;

import timber.log.Timber;


public final class YandexApplication extends Application {

    private NetworkComponent mNetworkComponent;
    private RefWatcher mRefWatcher;

    public static YandexApplication get(Context context) {
        return (YandexApplication) context.getApplicationContext();
    }

    @Override
    public final void onCreate() {
        super.onCreate();

        mRefWatcher = BuildConfig.DEBUG
//                ? LeakCanary.install(this)
                ? RefWatcher.DISABLED
                : RefWatcher.DISABLED;

        configureTimber();

        mNetworkComponent = DaggerNetworkComponent.builder()
                .appModule(new AppModule(this))
                .networkModule(new NetworkModule())
                .build();
    }

    public NetworkComponent getNetworkComponent() {
        return mNetworkComponent;
    }

    public RefWatcher getRefWatcher() {
        return mRefWatcher;
    }

    private void configureTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree() {
                // Adds the line number to the tag
                @Override protected String createStackElementTag(StackTraceElement element) {
                    return super.createStackElementTag(element) + ":" + element.getLineNumber();
                }
            });
        } else {
            // Release mode
            Timber.plant(new ReleaseTree());
        }
    }
}
