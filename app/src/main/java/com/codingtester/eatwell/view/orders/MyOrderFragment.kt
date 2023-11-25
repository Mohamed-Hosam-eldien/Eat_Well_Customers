package com.codingtester.eatwell.view.orders

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.codingtester.eatwell.databinding.FragmentMyOrderBinding
import com.codingtester.eatwell.view.register.WelcomeActivity
import com.codingtester.eatwell.viewmodel.DataViewModel
import com.codingtester.eatwell.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyOrderFragment : Fragment() {

    private lateinit var binding: FragmentMyOrderBinding
    private val viewModel: DataViewModel by viewModels()
    private val registerViewModel by viewModels<RegisterViewModel>()
    private val orderAdapter by lazy { OrderAdapter(mutableListOf()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyOrderBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.notRegisterLayout.visibility = View.VISIBLE

        registerViewModel.loginLiveData.observe(requireActivity()) { response ->
            if(response?.firebaseUser != null) {
                binding.dataLayout.visibility = View.VISIBLE
                binding.notRegisterLayout.visibility = View.GONE
                binding.imgEmptyCart.visibility = View.GONE
                viewModel.getAllOrders()
            } else {
                binding.imgEmptyCart.visibility = View.VISIBLE
            }
        }

        binding.recycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
        }

        viewModel.getAllOrdersLiveData.observe(requireActivity()) { orders ->
            if(orders.isNotEmpty()) {
                orderAdapter.setList(orders.reversed() as MutableList)
                binding.recycler.adapter = orderAdapter
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
            startActivity(Intent(requireContext(), WelcomeActivity::class.java))
        }
    }
}