package com.nd.ecommerce.retrofit

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.nd.ecommerce.data.Product
import com.nd.ecommerce.data.toProduct
class ProductsPagingSource(
    private val apiService: ProductsApiService,
    private val query: String?
) : PagingSource<Int, Product>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        return try {
            val currentPage = params.key ?: 0
            val pageSize = params.loadSize
            val skip = currentPage * pageSize

            val response = if (query.isNullOrBlank()) {
                apiService.getProducts(
                    limit = pageSize,
                    skip = skip
                )
            } else {
                apiService.searchProducts(
                    query = query,
                    limit = pageSize,
                    skip = skip
                )
            }

            val products = response.products.map { it.toProduct() }

            LoadResult.Page(
                data = products,
                prevKey = if (currentPage == 0) null else currentPage - 1,
                nextKey = if (products.isEmpty()) null else currentPage + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val page = state.closestPageToPosition(anchorPosition)
            page?.prevKey?.plus(1) ?: page?.nextKey?.minus(1)
        }
    }
}