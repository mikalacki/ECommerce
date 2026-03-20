package com.nd.ecommerce.retrofit

import com.nd.ecommerce.data.Product
import com.nd.ecommerce.data.toProduct

class ProductsRepository(
    private val apiService: ProductsApiService
) {
    suspend fun getProducts(): Result<List<Product>> {
        return try {
            val response = apiService.getProducts()

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.success(body.products.map { it.toProduct() })
                } else {
                    Result.failure(Exception("Response body is null"))
                }
            } else {
                Result.failure(Exception("HTTP error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}