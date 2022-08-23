package ai.joblio.ap

import ai.joblio.ap.util.Consts
import android.app.Application
import com.onesignal.OneSignal
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class FruitsApp : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        OneSignal.setAppId(Consts.ONESIGNAL_ID)
        OneSignal.initWithContext(applicationContext)
    }

    companion object {
        lateinit var gadId: String
    }
} 