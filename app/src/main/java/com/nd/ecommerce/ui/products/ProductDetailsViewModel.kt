package com.nd.ecommerce.ui.products

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nd.ecommerce.data.ProductDetails
import com.nd.ecommerce.retrofit.ProductsRepository
import com.nd.ecommerce.retrofit.RetrofitClient
import kotlinx.coroutines.launch

class ProductDetailsViewModel : ViewModel() {

    private val repository = ProductsRepository(RetrofitClient.productsApi)

    private val _productDetails = MutableLiveData<ProductDetails>()
    val productDetails: LiveData<ProductDetails> = _productDetails

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun fetchProductDetails(productId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            val result = repository.getProductDetails(productId)

            result.onSuccess {
                _productDetails.value = it
            }.onFailure {
                _error.value = it.message
            }

            _isLoading.value = false
        }
    }
}