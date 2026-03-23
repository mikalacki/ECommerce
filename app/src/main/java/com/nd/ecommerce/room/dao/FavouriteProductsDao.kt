package com.nd.ecommerce.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nd.ecommerce.room.entity.FavouriteProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouriteProductsDao {

    @Query("SELECT * FROM favourite_products")
    fun getAllFavouriteProducts(): Flow<List<FavouriteProductEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favourite_products WHERE id = :productId)")
    fun isFavouriteFlow(productId: Int): Flow<Boolean>

    @Query("SELECT EXISTS(SELECT 1 FROM favourite_products WHERE id = :productId)")
    suspend fun isFavourite(productId: Int): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavouriteProduct(product: FavouriteProductEntity)

    @Query("DELETE FROM favourite_products WHERE id = :productId")
    suspend fun deleteFavouriteProduct(productId: Int)

    @Query("SELECT id FROM favourite_products")
    fun getFavouriteProductIds(): Flow<List<Int>>
}