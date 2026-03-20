package com.nd.ecommerce.ui.products

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nd.ecommerce.data.Product
import com.nd.ecommerce.retrofit.ProductsRepository
import com.nd.ecommerce.retrofit.RetrofitClient
import kotlinx.coroutines.launch

class ProductsViewModel : ViewModel() {

    private val repository = ProductsRepository(RetrofitClient.productsApi)

    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> = _products

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun fetchProducts() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            val result = repository.getProducts()

            result.onSuccess {
                _products.value = it
            }.onFailure {
                _error.value = it.message ?: "Unknown error"
            }

            _isLoading.value = false
        }
    }
}