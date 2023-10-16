package com.aeryz.retrofitexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.aeryz.retrofitexample.adapter.ProductAdapter
import com.aeryz.retrofitexample.adapter.ProductViewModel
import com.aeryz.retrofitexample.databinding.ActivityMainBinding
import com.aeryz.retrofitexample.model.Product
import com.aeryz.retrofitexample.repository.ProductRepository
import com.aeryz.retrofitexample.repository.ProductRepositoryImpl
import com.aeryz.retrofitexample.service.ProductService
import com.aeryz.retrofitexample.utils.GenericViewModelFactory
import com.aeryz.retrofitexample.utils.proceedWhen
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val viewModel: ProductViewModel by viewModels {
        val productService = ProductService.invoke()
        val productRepository : ProductRepository = ProductRepositoryImpl(productService)
        GenericViewModelFactory.create(ProductViewModel(productRepository))
    }
    private val productAdapter: ProductAdapter by lazy {
        ProductAdapter(emptyList())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupRecyclerView()
        fetchProducts()
        observeViewModel()
    }

    private fun fetchProducts() {
        viewModel.fetchProducts()
    }

    private fun observeViewModel() {
        viewModel.products.observe(this) {
            lifecycleScope.launch {
                it?.proceedWhen(
                    doOnSuccess = { handleSuccess(it.payload?.products) },
                    doOnError = { handleError(it.exception) },
                    doOnLoading = { handleLoading() },
                    doOnEmpty = { handleEmpty() }
                )
            }
        }
    }

    private fun setupRecyclerView() {
        binding.rvProducts.adapter = productAdapter
        binding.rvProducts.layoutManager = LinearLayoutManager(this)
    }

    private fun handleSuccess(products: List<Product>?) {
        productAdapter.updateData(products ?: emptyList())
        binding.pbLoading.isVisible = false
        binding.tvError.isVisible = false
    }

    private fun handleError(exception: Exception?) {
        binding.tvError.isVisible = true
        binding.tvError.text = "Error : $exception"
        binding.pbLoading.isVisible = false
    }

    private fun handleLoading() {
        binding.pbLoading.isVisible = true
        binding.tvError.isVisible = false
    }

    private fun handleEmpty() {
        binding.tvError.isVisible = true
        binding.tvError.text = "Data is Empty"
        binding.pbLoading.isVisible = false
    }
}
