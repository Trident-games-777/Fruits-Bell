package ai.joblio.ap.di

import ai.joblio.ap.db.UrlDatabase
import ai.joblio.ap.reposetories.FruitRepository
import ai.joblio.ap.util.Consts
import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideFruitRepository() = FruitRepository()

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext app: Context) = Room.databaseBuilder(
        app,
        UrlDatabase::class.java,
        Consts.DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun provideUrlDao(db: UrlDatabase) = db.getUrlDao()
}