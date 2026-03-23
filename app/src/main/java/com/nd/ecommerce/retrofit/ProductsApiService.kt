package com.nd.ecommerce.retrofit

import com.nd.ecommerce.retrofit.dto.ProductDto
import com.nd.ecommerce.retrofit.dto.ProductsResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductsApiService {

    @GET("products")
    suspend fun getProducts(
        @Query("limit") limit: Int, @Query("skip") skip: Int
    ): ProductsResponseDto

    @GET("products/{id}")
    suspend fun getProductById(
        @Path("id") id: Int
    ): ProductDto

    @GET("products/search")
    suspend fun searchProducts(
        @Query("q") query: String,
        @Query("limit") limit: Int,
        @Query("skip") skip: Int
    ): ProductsResponseDto
}