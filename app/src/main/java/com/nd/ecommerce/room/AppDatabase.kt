package com.nd.ecommerce.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nd.ecommerce.room.dao.FavouriteProductsDao
import com.nd.ecommerce.room.entity.FavouriteProductEntity

@Database(
    entities = [FavouriteProductEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favouriteProductsDao(): FavouriteProductsDao
}