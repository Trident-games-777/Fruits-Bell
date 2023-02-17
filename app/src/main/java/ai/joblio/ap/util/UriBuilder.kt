package ai.joblio.ap.util

import androidx.core.net.toUri
import com.trident.media.helper.network.models.postmodel.ConversionDataObject
import java.util.*

class UriBuilder {

    fun createUrl(
        data: ConversionDataObject?,
        adId: String
    ): String {
        val url = Consts.BASE_URL.toUri().buildUpon().apply {
            appendQueryParameter(Consts.SECURE_GET_PARAMETR, Consts.SECURE_KEY)
            appendQueryParameter(Consts.DEV_TMZ_KEY, TimeZone.getDefault().id)
            appendQueryParameter(Consts.GADID_KEY, adId)
            appendQueryParameter(Consts.DEEPLINK_KEY, "null")
            appendQueryParameter(Consts.SOURCE_KEY, data?.source.toString())
            appendQueryParameter(Consts.AF_ID_KEY, data?.externalId.toString())
            appendQueryParameter(Consts.ADSET_ID_KEY, data?.adEventId.toString())
            appendQueryParameter(Consts.CAMPAIGN_ID_KEY, data?.campaignId.toString())
            appendQueryParameter(Consts.APP_COMPAIGN_KEY, data?.campaignName.toString())
            appendQueryParameter(Consts.ADSET_KEY, data?.adType.toString())
            appendQueryParameter(Consts.ADGROUP_KEY, data?.adGroupName.toString())
            appendQueryParameter(Consts.ORIG_COST_KEY, "null")
            appendQueryParameter(Consts.AF_SITEID_KEY, data?.networkType.toString())
        }.toString()
        return url
    }
}