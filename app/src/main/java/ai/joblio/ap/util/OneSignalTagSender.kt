package ai.joblio.ap.util

import com.onesignal.OneSignal

class OneSignalTagSender {
    fun sendTag(tg: String) {
        OneSignal.sendTag("key2", tg)
    }

}