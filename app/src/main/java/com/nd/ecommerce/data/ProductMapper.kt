package com.nd.ecommerce.data

import com.nd.ecommerce.retrofit.dto.ProductDto
import com.nd.ecommerce.room.entity.FavouriteProductEntity

fun ProductDto.toProduct(): Product {
    return Product(
        id = id, title = title, description = description, price = price, thumbnail = thumbnail
    )
}

fun ProductDto.toProductDetails(): ProductDetails {
    return ProductDetails(
        id = id, title = title, description = description, price = price, thumbnail = thumbnail
    )
}

fun Product.toFavouriteEntity(): FavouriteProductEntity {
    return FavouriteProductEntity(
        id = id, title = title, description = description, price = price, thumbnail = thumbnail
    )
}

fun ProductDetails.toFavouriteEntity(): FavouriteProductEntity {
    return FavouriteProductEntity(
        id = id, title = title, description = description, price = price, thumbnail = thumbnail
    )
}

fun FavouriteProductEntity.toProduct(): Product {
    return Product(
        id = id, title = title, description = description, price = price, thumbnail = thumbnail
    )
}