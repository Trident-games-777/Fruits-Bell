package ai.joblio.ap.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [UrlEntity::class],
    version = 1
)
abstract class UrlDatabase : RoomDatabase() {

    abstract val fruitDao: UrlDao

}
