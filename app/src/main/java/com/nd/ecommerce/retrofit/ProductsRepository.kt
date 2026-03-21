package com.nd.ecommerce.retrofit

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.nd.ecommerce.data.Product
import com.nd.ecommerce.data.ProductDetails
import com.nd.ecommerce.data.toProduct
import com.nd.ecommerce.data.toProductDetails
import kotlinx.coroutines.flow.Flow

class ProductsRepository(
    private val apiService: ProductsApiService
) {
    fun getProductsPaged(): Flow<PagingData<Product>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5, initialLoadSize = 5, enablePlaceholders = false
            ), pagingSourceFactory = {
                ProductsPagingSource(apiService)
            }).flow
    }

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

    suspend fun getProductDetails(productId: Int): Result<ProductDetails> {
        return try {
            val response = apiService.getProductById(productId)
            Result.success(response.toProductDetails())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}