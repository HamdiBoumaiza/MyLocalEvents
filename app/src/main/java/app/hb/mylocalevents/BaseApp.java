package app.hb.mylocalevents;

import android.app.Application;

import timber.log.Timber;

public class BaseApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG)
            Timber.plant(new Timber.DebugTree());


    }
}
