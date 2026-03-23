package com.nd.ecommerce.ui.products.favourite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.nd.ecommerce.data.Product
import com.nd.ecommerce.retrofit.ProductsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouriteProductsViewModel @Inject constructor(
    private val repository: ProductsRepository
) : ViewModel() {

    val favouriteProducts = repository
        .getFavouriteProducts()
        .asLiveData()

    fun toggleFavourite(product: Product) {
        viewModelScope.launch {
            repository.toggleFavourite(product)
        }
    }
}