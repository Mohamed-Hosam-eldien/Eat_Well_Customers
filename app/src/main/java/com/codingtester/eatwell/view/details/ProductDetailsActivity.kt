package com.codingtester.eatwell.view.details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.codingtester.eatwell.databinding.ActivityProductDetailsBinding
import com.codingtester.eatwell.model.pojo.OrderLine
import com.codingtester.eatwell.model.pojo.Product
import com.codingtester.eatwell.viewmodel.DataViewModel
import com.codingtester.eatwell.viewmodel.RegisterViewModel
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductDetailsBinding
    private lateinit var product: Product
    private val viewModel: DataViewModel by viewModels()
    private val registerViewModel by viewModels<RegisterViewModel>()
    private var selectedSize = "Medium"
    private var isUserLogin = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        (intent.getSerializableExtra("product") as Product).let { currentProduct ->
            product = currentProduct
            binding.txtName.text = product.name
            binding.txtPrice.text = product.price?.medium.toString()

            if(product.ingredients.isNotEmpty()) {
                binding.txtIngDetails.text = product.ingredients
            } else {
                binding.txtIngDetails.visibility = View.GONE
                binding.txtIng.visibility = View.GONE
            }

            if(product.price?.famili == 0) {
                binding.chipFamili.visibility = View.GONE
            }
            if(product.price?.xxl == 0) {
                binding.chipXXL.visibility = View.GONE
            }

            binding.txtTotalPrice.text = product.price?.medium.toString()

            Glide.with(this)
                .load(product.image)
                .into(binding.imageView)
        }

        registerViewModel.loginLiveData.observe(this) { response ->
            if(response?.firebaseUser != null) {
                isUserLogin = true
            }
        }

        binding.btnIncrease.setOnClickListener {
            if(binding.txtQuantity.text.toString().toInt() < 20) {
                val newValue = binding.txtQuantity.text.toString().toInt() + 1
                binding.txtQuantity.text = newValue.toString()
                binding.txtTotalPrice.text = (newValue * binding.txtPrice.text.toString().toInt()).toString()
            }
        }

        binding.btnDecrease.setOnClickListener {
            if(binding.txtQuantity.text.toString().toInt() > 1) {
                val newValue = binding.txtQuantity.text.toString().toInt() - 1
                binding.txtQuantity.text = newValue.toString()
                binding.txtTotalPrice.text = (newValue * binding.txtPrice.text.toString().toInt()).toString()
            }
        }

        binding.chipGroup.setOnCheckedChangeListener { group, selectedId ->
            selectedSize = group.findViewById<Chip>(selectedId).text.toString()
            var price = 0

            when(selectedSize) {
                "Medium" -> {
                    price = product.price?.medium!!
                }
                "Famili" -> {
                    price = product.price?.famili!!
                }
                "XXL" -> {
                    price = product.price?.xxl!!
                }
            }
            changePricesData(price)
        }

        binding.btnAddToCart.setOnClickListener {
            if(isUserLogin) {
                addItemToCart()
            } else {
                SignupDialog().show(supportFragmentManager, "signUp")
            }
        }

        viewModel.orderLineLiveData.observe(this) { state ->
            if(state == "Success") {
                Toast.makeText(this, "${product.name} has been added successfully", Toast.LENGTH_SHORT).show()
                binding.btnAddToCart.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
            } else {
                Toast.makeText(this, "oops, please try again", Toast.LENGTH_SHORT).show()
                binding.btnAddToCart.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun addItemToCart() {
        binding.btnAddToCart.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE

        val orderLine = OrderLine(
            product.id,
            product.name,
            selectedSize,
            product.image,
            binding.txtQuantity.text.toString(),
            binding.txtPrice.text.toString().toInt(),
            binding.txtTotalPrice.text.toString().toInt()
        )

        viewModel.addToCart(orderLine)
    }

    private fun changePricesData(price: Int) {
        binding.txtPrice.text = price.toString()
        binding.txtTotalPrice.text = (price * binding.txtQuantity.text.toString().toInt()).toString()
    }
}