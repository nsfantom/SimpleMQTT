package tm.fantom.simplemqtt;

import android.app.Application;

import timber.log.Timber;

/**
 * Created by fantom on 11-Mar-18.
 */

public final class AppController extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
