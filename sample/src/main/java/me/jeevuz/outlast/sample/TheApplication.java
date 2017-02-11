package me.jeevuz.outlast.sample;

import android.app.Application;

import java.util.Arrays;
import java.util.List;

import me.jeevuz.outlast.BuildConfig;
import timber.log.Timber;


/**
 * @author Vasili Chyrvon (vasili.chyrvon@gmail.com)
 */
public class TheApplication extends Application {

    // Bad words list, sorry :)
    public static final List<String> BAD_WORDS = Arrays.asList(
            "shit", "fuck", "damn", "bitch", "crap", "dick", "piss", "pussy",
            "fag", "asshole", "cock", "bastard", "darn", "douche", "slut"
    );

    @Override
    public void onCreate() {
        super.onCreate();
        initLogging();
    }

    private void initLogging() {
        // Configure Timber
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
