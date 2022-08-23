package ai.joblio.ap.reposetories

import ai.joblio.ap.db.UrlEntity
import androidx.lifecycle.LiveData

interface FruitRepositoryInt {

    suspend fun insertUrl(url: UrlEntity)

    fun getUrl(): LiveData<UrlEntity>
}