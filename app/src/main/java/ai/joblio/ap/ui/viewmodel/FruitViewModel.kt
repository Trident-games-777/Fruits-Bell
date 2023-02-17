package ai.joblio.ap.ui.viewmodel

import ai.joblio.ap.FruitsApp.Companion.gadId
import ai.joblio.ap.db.UrlEntity
import ai.joblio.ap.reposetories.FruitRepositoryInt
import ai.joblio.ap.util.OneSignalTagSender
import ai.joblio.ap.util.UriBuilder
import android.app.Activity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trident.media.helper.TridentConversionListener
import com.trident.media.helper.TridentLib
import com.trident.media.helper.network.models.postmodel.ConversionDataObject
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

    fun fetchData(activity: Activity) {
        TridentLib.getInstance().init(activity, object : TridentConversionListener {
            override fun onConversionDataFail(errorMessage: String) {
                oneSignalTagSender.sendTag("null")
                urlLiveData.postValue(uriBuilder.createUrl(null, gadId))
            }

            override fun onConversionDataSuccess(data: ConversionDataObject?) {
                if (data != null) oneSignalTagSender.sendTag(
                    data.campaignName?.substringBefore(
                        "_"
                    ).toString()
                )
                else oneSignalTagSender.sendTag("organic")
                urlLiveData.postValue(uriBuilder.createUrl(data, gadId))
            }
        })
    }
}