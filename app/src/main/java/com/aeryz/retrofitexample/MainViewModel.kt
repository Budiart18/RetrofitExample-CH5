package com.aeryz.retrofitexample

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aeryz.retrofitexample.model.ProductResponse
import com.aeryz.retrofitexample.service.ProductService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    val service: ProductService by lazy {
        ProductService.invoke()
    }

    val responseLiveData = MutableLiveData<ProductResponse>()

    fun getProducts() {
        viewModelScope.launch(Dispatchers.IO){
            try {
                val response = service.getProducts()
                responseLiveData.postValue(response)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}