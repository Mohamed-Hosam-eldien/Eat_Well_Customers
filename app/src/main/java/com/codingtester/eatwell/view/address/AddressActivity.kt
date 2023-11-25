package com.codingtester.eatwell.view.address

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.codingtester.eatwell.databinding.ActivityAddressBinding
import com.codingtester.eatwell.model.pojo.Address
import com.codingtester.eatwell.viewmodel.DataViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddressActivity : AppCompatActivity(), ClickToAddress {

    private lateinit var binding: ActivityAddressBinding
    private val viewModel: DataViewModel by viewModels()
    private val addressAdapter by lazy { AddressAdapter(mutableListOf(), this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recycler.apply {
            layoutManager = LinearLayoutManager(this@AddressActivity)
        }

        viewModel.getAddressData()
        viewModel.getAddressLiveData.observe(this) { addresses ->
            if(addresses.isNotEmpty()) {
                addressAdapter.setList(addresses as MutableList)
                binding.recycler.adapter = addressAdapter
                binding.progress.visibility = View.GONE
                binding.imgEmpty.visibility = View.GONE
            } else {
                binding.progress.visibility = View.GONE
                binding.imgEmpty.visibility = View.VISIBLE
            }
        }

        binding.imgAdd.setOnClickListener {
            addNewAddress()
        }

        binding.imgBack.setOnClickListener { finish() }
    }

    private fun addNewAddress() {
        AddAddressDialog().show(supportFragmentManager, "")
    }

    override fun clickToDeleteAddress(currentAddress: Address) {
        AlertDialog.Builder(this)
            .setTitle("Delete Address!")
            .setMessage("Are you sure to delete this address?")
            .setPositiveButton("Delete") { dialog, _ ->
                addressAdapter.updateAfterRemovedItem()
                viewModel.removeAddress(currentAddress)
                viewModel.getAddressData()
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}