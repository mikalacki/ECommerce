package com.nd.ecommerce.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    private val _bnbVisibility: MutableLiveData<Boolean> = MutableLiveData(true)
    val bnbVisibility: LiveData<Boolean> = _bnbVisibility

    fun setBnbVisibility(value: Boolean) {
        _bnbVisibility.postValue(value)
    }
}