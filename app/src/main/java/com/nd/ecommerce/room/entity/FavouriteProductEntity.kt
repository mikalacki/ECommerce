package com.nd.ecommerce.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourite_products")
data class FavouriteProductEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val description: String,
    val price: Double,
    val thumbnail: String
)