package ai.joblio.ap.di

import ai.joblio.ap.reposetories.FruitRepository
import ai.joblio.ap.reposetories.FruitRepositoryInt
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    @Singleton
    fun bindTripRepository(
        repository: FruitRepository // here the class
    ): FruitRepositoryInt // here the interface that the class implements
}