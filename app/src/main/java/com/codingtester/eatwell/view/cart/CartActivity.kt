package com.codingtester.eatwell.view.cart

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.codingtester.eatwell.databinding.ActivityCartBinding
import com.codingtester.eatwell.view.checkout.CheckoutActivity
import com.codingtester.eatwell.view.register.WelcomeActivity
import com.codingtester.eatwell.viewmodel.DataViewModel
import com.codingtester.eatwell.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartActivity : AppCompatActivity(), OnClickToDeleteCart {

    private lateinit var binding: ActivityCartBinding
    private val viewModel: DataViewModel by viewModels()
    private val registerViewModel by viewModels<RegisterViewModel>()
    private val cartAdapter by lazy { CartAdapter(mutableListOf(), this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerViewModel.loginLiveData.observe(this) { response ->
            if(response?.firebaseUser != null) {
                binding.dataLayout.visibility = View.VISIBLE
                binding.notRegisterLayout.visibility = View.GONE
                binding.imgEmpty.visibility = View.GONE

                viewModel.getCart()
                viewModel.getOrderPrices()
            } else {
                binding.imgEmpty.visibility = View.VISIBLE
            }
        }

        binding.notRegisterLayout.visibility = View.VISIBLE

        binding.recycler.apply {
            layoutManager = LinearLayoutManager(this@CartActivity)
        }

        viewModel.cartLiveData.observe(this) { orderLines ->
            if(orderLines.isNotEmpty()) {
                cartAdapter.setList(orderLines as MutableList)
                binding.recycler.adapter = cartAdapter
                binding.progress.visibility = View.GONE
                binding.dataLayout.visibility = View.VISIBLE
                binding.imgEmpty.visibility = View.GONE
            } else {
                binding.progress.visibility = View.GONE
                binding.dataLayout.visibility = View.GONE
                binding.imgEmpty.visibility = View.VISIBLE
            }
        }

        viewModel.orderTotalPriceLiveData.observe(this) { totalPrice ->
            binding.txtTotalPrice.text = totalPrice.toString()
        }

        binding.btnSign.setOnClickListener {
            startActivity(Intent(this, WelcomeActivity::class.java))
        }

        binding.cardSubmitOrder.setOnClickListener {
            val intent = Intent(this, CheckoutActivity::class.java)
            intent.putExtra("totalPrice", binding.txtTotalPrice.text.toString())
            startActivity(intent)
        }

        binding.imgBack.setOnClickListener { finish() }
    }

    override fun onDelete(id: String) {
        AlertDialog.Builder(this)
            .setTitle("Delete Item")
            .setMessage("Are you sure to delete this item?")
            .setPositiveButton("Delete") { dialog, _ ->
                viewModel.deleteOrderLine(id)
                viewModel.getCart()
                viewModel.getOrderPrices()
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}