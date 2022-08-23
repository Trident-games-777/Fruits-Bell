package ai.joblio.ap.reposetories

import ai.joblio.ap.db.UrlDao
import ai.joblio.ap.db.UrlEntity
import javax.inject.Inject

class FruitRepository @Inject constructor(
) {
    @Inject
    lateinit var urlDao: UrlDao

    suspend fun insertUrl(url: UrlEntity) = urlDao.insertUrl(url)

    fun getUrl() = urlDao.getUrl()
}