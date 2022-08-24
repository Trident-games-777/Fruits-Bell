package ai.joblio.ap.ui.viewmodel

import ai.joblio.ap.FruitsApp.Companion.gadId
import ai.joblio.ap.db.UrlEntity
import ai.joblio.ap.reposetories.FruitRepositoryInt
import ai.joblio.ap.util.Consts
import ai.joblio.ap.util.OneSignalTagSender
import ai.joblio.ap.util.UriBuilder
import android.app.Activity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import com.facebook.applinks.AppLinkData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FruitViewModel @Inject constructor(
    private val fruitRepository: FruitRepositoryInt
) : ViewModel() {

    private val uriBuilder = UriBuilder()
    private val oneSignalTagSender = OneSignalTagSender()

    val urlLiveData: MutableLiveData<String> = MutableLiveData()

    fun insertUrlToDB(url: UrlEntity) = viewModelScope.launch {
        fruitRepository.insertUrl(url)
    }

    fun getUrlFromDb() = fruitRepository.getUrl()


    fun fetchDeeplink(activity: Activity) {
        AppLinkData.fetchDeferredAppLinkData(activity) {
            val deeplink = it?.targetUri.toString()
            if (deeplink == "null") {
                fetchData(activity)
            } else {
                urlLiveData.postValue(uriBuilder.createUrl(deeplink, null, activity, gadId))
            }
        }
    }

    private fun fetchData(activity: Activity) {
        AppsFlyerLib.getInstance().init(Consts.APPS_DEV_KEY, object : AppsFlyerConversionListener {
            override fun onConversionDataSuccess(data: MutableMap<String, Any>?) {
                oneSignalTagSender.sendTag("null", data)
                urlLiveData.postValue(uriBuilder.createUrl("null", data, activity, gadId))
            }

            override fun onConversionDataFail(p0: String?) {
                TODO("Not yet implemented")
            }

            override fun onAppOpenAttribution(p0: MutableMap<String, String>?) {
                TODO("Not yet implemented")
            }

            override fun onAttributionFailure(p0: String?) {
                TODO("Not yet implemented")
            }

        }, activity)
        AppsFlyerLib.getInstance().start(activity)
    }
}