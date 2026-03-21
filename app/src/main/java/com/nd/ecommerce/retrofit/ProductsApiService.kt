package com.nd.ecommerce.retrofit

import com.nd.ecommerce.retrofit.dto.ProductDto
import com.nd.ecommerce.retrofit.dto.ProductsResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductsApiService {

    @GET("products")
    suspend fun getProducts(
        @Query("limit") limit: Int = 30, @Query("skip") skip: Int = 0
    ): Response<ProductsResponseDto>

    @GET("products")
    suspend fun getProductsPaged(
        @Query("limit") limit: Int, @Query("skip") skip: Int
    ): ProductsResponseDto

    @GET("products/{id}")
    suspend fun getProductById(
        @Path("id") id: Int
    ): ProductDto
}