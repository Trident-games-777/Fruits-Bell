package ai.joblio.ap.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "url_table")
data class UrlEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var flag: Int = 0,
    var url: String? = null
)