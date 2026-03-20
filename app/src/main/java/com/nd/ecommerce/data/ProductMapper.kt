package com.nd.ecommerce.data

import com.nd.ecommerce.retrofit.dto.ProductDto


fun ProductDto.toProduct(): Product {
    return Product(
        id = id,
        title = title,
        description = description,
        price = price,
        rating = rating,
        thumbnail = thumbnail
    )
}