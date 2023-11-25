package com.codingtester.eatwell.view.reserve

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.codingtester.eatwell.databinding.ActivityReservationsBinding
import com.codingtester.eatwell.view.register.WelcomeActivity
import com.codingtester.eatwell.viewmodel.DataViewModel
import com.codingtester.eatwell.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReservationsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReservationsBinding
    private val viewModel: DataViewModel by viewModels()
    private val registerViewModel by viewModels<RegisterViewModel>()
    private val reservationAdapter by lazy { ReservationAdapter(mutableListOf()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReservationsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.notRegisterLayout.visibility = View.VISIBLE

        registerViewModel.loginLiveData.observe(this) { response ->
            if(response?.firebaseUser != null) {
                binding.dataLayout.visibility = View.VISIBLE
                binding.notRegisterLayout.visibility = View.GONE
                binding.imgEmptyCart.visibility = View.GONE
                viewModel.getAllReserve()
            } else {
                binding.imgEmptyCart.visibility = View.VISIBLE
            }
        }

        binding.recycler.apply {
            layoutManager = LinearLayoutManager(this@ReservationsActivity)
        }

        viewModel.getReservedLiveData.observe(this) { allReserves ->
            if(allReserves.isNotEmpty()) {
                reservationAdapter.setList(allReserves.reversed() as MutableList)
                binding.recycler.adapter = reservationAdapter
                binding.progress.visibility = View.GONE
                binding.dataLayout.visibility = View.VISIBLE
                binding.imgEmptyCart.visibility = View.GONE
            } else {
                binding.progress.visibility = View.GONE
                binding.dataLayout.visibility = View.GONE
                binding.imgEmptyCart.visibility = View.VISIBLE
            }
        }

        binding.btnSign.setOnClickListener {
            startActivity(Intent(this, WelcomeActivity::class.java))
        }
    }
}