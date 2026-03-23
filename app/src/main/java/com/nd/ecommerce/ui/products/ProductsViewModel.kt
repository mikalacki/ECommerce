package com.nd.ecommerce.ui.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.nd.ecommerce.data.Product
import com.nd.ecommerce.retrofit.ProductsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val repository: ProductsRepository
) : ViewModel() {

    private val searchQuery = MutableStateFlow("")

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val productsPagingData = searchQuery
        .debounce(300L)
        .distinctUntilChanged()
        .flatMapLatest { query ->
            repository.getProductsPaged(query)
        }
        .cachedIn(viewModelScope)

    val favouriteProductIds = repository
        .getFavouriteProductIds()
        .asLiveData()

    fun updateSearchQuery(query: String) {
        searchQuery.value = query
    }

    fun toggleFavourite(product: Product) {
        viewModelScope.launch {
            repository.toggleFavourite(product)
        }
    }
}