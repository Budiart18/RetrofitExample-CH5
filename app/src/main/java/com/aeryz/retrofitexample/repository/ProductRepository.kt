package com.aeryz.retrofitexample.repository

import com.aeryz.retrofitexample.model.ProductResponse
import com.aeryz.retrofitexample.service.ProductService
import com.aeryz.retrofitexample.utils.ResultWrapper
import com.aeryz.retrofitexample.utils.proceedFlow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart

interface ProductRepository {
    suspend fun getProducts(): Flow<ResultWrapper<ProductResponse>>
}

class ProductRepositoryImpl(private val productService: ProductService) : ProductRepository {

    override suspend fun getProducts(): Flow<ResultWrapper<ProductResponse>> {
        return proceedFlow {
            productService.getProducts()
        }.onStart {
            emit(ResultWrapper.Loading())
            delay(2000)
        }
    }
}