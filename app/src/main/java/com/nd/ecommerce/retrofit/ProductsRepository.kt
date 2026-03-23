package com.nd.ecommerce.retrofit

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.nd.ecommerce.data.Product
import com.nd.ecommerce.data.ProductDetails
import com.nd.ecommerce.data.toFavouriteEntity
import com.nd.ecommerce.data.toProduct
import com.nd.ecommerce.data.toProductDetails
import com.nd.ecommerce.room.dao.FavouriteProductsDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductsRepository @Inject constructor(
    private val apiService: ProductsApiService,
    private val favouriteProductsDao: FavouriteProductsDao
) {

    fun getProductsPaged(query: String?): Flow<PagingData<Product>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                initialLoadSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                ProductsPagingSource(apiService, query)
            }
        ).flow
    }

    fun getFavouriteProducts(): Flow<List<Product>> {
        return favouriteProductsDao.getAllFavouriteProducts().map { list ->
            list.map { it.toProduct() }
        }
    }

    fun isFavouriteFlow(productId: Int): Flow<Boolean> {
        return favouriteProductsDao.isFavouriteFlow(productId)
    }

    fun getFavouriteProductIds(): Flow<Set<Int>> {
        return favouriteProductsDao.getFavouriteProductIds().map { it.toSet() }
    }

    suspend fun toggleFavourite(product: Product) {
        val isFavourite = favouriteProductsDao.isFavourite(product.id)
        if (isFavourite) {
            favouriteProductsDao.deleteFavouriteProduct(product.id)
        } else {
            favouriteProductsDao.insertFavouriteProduct(product.toFavouriteEntity())
        }
    }

    suspend fun toggleFavourite(productDetails: ProductDetails) {
        val isFavourite = favouriteProductsDao.isFavourite(productDetails.id)
        if (isFavourite) {
            favouriteProductsDao.deleteFavouriteProduct(productDetails.id)
        } else {
            favouriteProductsDao.insertFavouriteProduct(productDetails.toFavouriteEntity())
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