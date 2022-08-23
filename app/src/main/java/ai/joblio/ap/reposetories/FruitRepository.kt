package ai.joblio.ap.reposetories

import ai.joblio.ap.db.UrlDao
import ai.joblio.ap.db.UrlEntity
import javax.inject.Inject

class FruitRepository @Inject constructor(
    val fruitsDao: UrlDao
): FruitRepositoryInt {
    @Inject
    lateinit var urlDao: UrlDao

    override suspend fun insertUrl(url: UrlEntity) = urlDao.insertUrl(url)

    override fun getUrl() = urlDao.getUrl()
}