package com.codingtester.eatwell.view.products

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.codingtester.eatwell.databinding.ActivityProductsBinding
import com.codingtester.eatwell.model.pojo.Product
import com.codingtester.eatwell.view.details.ProductDetailsActivity
import com.codingtester.eatwell.viewmodel.DataViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductsActivity : AppCompatActivity(), ClickToProduct {

    private lateinit var binding: ActivityProductsBinding
    private val viewModel: DataViewModel by viewModels()
    private val productAdapter by lazy { ProductAdapter(emptyList(), this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recycler.apply {
            layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        }

        intent.getStringExtra("category")?.let { category ->
            viewModel.getProductsByCategory(category)
            binding.txtCategory.text = category
        }

        viewModel.productsLiveData.observe(this) { products ->
            productAdapter.setList(products)
            binding.recycler.adapter = productAdapter
            binding.progress.visibility = View.GONE
        }

        binding.imgBack.setOnClickListener { finish() }
    }

    override fun onClickToProduct(product: Product) {
        val intent = Intent(this, ProductDetailsActivity::class.java)
        intent.putExtra("product", product)
        startActivity(intent)
    }
}