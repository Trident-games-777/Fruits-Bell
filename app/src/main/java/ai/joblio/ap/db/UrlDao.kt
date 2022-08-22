package ai.joblio.ap.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UrlDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUrl(url: UrlEntity)

    @Query("SELECT * FROM url_table LIMIT 1")
    fun getUrl(): LiveData<UrlEntity>
}