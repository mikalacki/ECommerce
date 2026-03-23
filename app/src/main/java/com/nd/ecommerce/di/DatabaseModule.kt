package com.nd.ecommerce.di

import android.content.Context
import androidx.room.Room
import com.nd.ecommerce.room.AppDatabase
import com.nd.ecommerce.room.dao.FavouriteProductsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "shop_database"
        ).build()
    }

    @Provides
    fun provideFavouriteProductsDao(
        appDatabase: AppDatabase
    ): FavouriteProductsDao {
        return appDatabase.favouriteProductsDao()
    }
}