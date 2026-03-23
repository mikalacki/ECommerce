package com.nd.ecommerce.ui.products

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nd.ecommerce.R
import com.nd.ecommerce.data.ProductDetails
import com.nd.ecommerce.retrofit.ProductsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    private val repository: ProductsRepository
) : ViewModel() {

    private val _productDetails = MutableLiveData<ProductDetails>()
    val productDetails: LiveData<ProductDetails> = _productDetails

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _isFavourite = MutableLiveData(false)
    val isFavourite: LiveData<Boolean> = _isFavourite

    fun fetchProductDetails(productId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            val result = repository.getProductDetails(productId)

            result.onSuccess {
                _productDetails.value = it
            }.onFailure {
                _error.value = it.message ?: context.getString(R.string.unknown_error)
            }

            _isLoading.value = false
        }
    }

    fun observeFavourite(productId: Int) {
        viewModelScope.launch {
            repository.isFavouriteFlow(productId).collectLatest {
                _isFavourite.postValue(it)
            }
        }
    }

    fun toggleFavourite() {
        val product = _productDetails.value ?: return
        viewModelScope.launch {
            repository.toggleFavourite(product)
        }
    }
}