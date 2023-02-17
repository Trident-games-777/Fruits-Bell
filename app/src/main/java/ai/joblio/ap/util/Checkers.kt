package ai.joblio.ap.util

import android.app.Activity
import android.provider.Settings
import java.io.File

class Checkers {

    fun isFirstLaunch() {

    }

    fun isDeviceSecured(activity: Activity): Boolean {
        return checkADB(activity) == "1"
    }

    private fun checkADB(activity: Activity): String {
        return Settings.Global.getString(activity.contentResolver, Settings.Global.ADB_ENABLED)
            ?: "null"
    }
}