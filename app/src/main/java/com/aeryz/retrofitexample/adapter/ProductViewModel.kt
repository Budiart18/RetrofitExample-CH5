package com.aeryz.retrofitexample.adapter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aeryz.retrofitexample.model.ProductResponse
import com.aeryz.retrofitexample.repository.ProductRepository
import com.aeryz.retrofitexample.utils.ResultWrapper
import kotlinx.coroutines.launch

class ProductViewModel(private val repository: ProductRepository) : ViewModel() {
    private val _products = MutableLiveData<ResultWrapper<ProductResponse>>()
    val products: LiveData<ResultWrapper<ProductResponse>>
        get() = _products

    fun fetchProducts() {
        viewModelScope.launch {
            repository.getProducts().collect{result ->
                _products.value = result
            }
        }
    }
}