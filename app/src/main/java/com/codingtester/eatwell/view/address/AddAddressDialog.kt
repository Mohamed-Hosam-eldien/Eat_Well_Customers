package com.codingtester.eatwell.view.address

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.codingtester.eatwell.databinding.AddAddressDialogBinding
import com.codingtester.eatwell.model.pojo.Address
import com.codingtester.eatwell.viewmodel.DataViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddAddressDialog : DialogFragment() {

    private lateinit var binding: AddAddressDialogBinding
    private val viewModel: DataViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = AddAddressDialogBinding.inflate(layoutInflater)

        binding.btnAddAddress.setOnClickListener {
            if(isViewsNotEmpty()) {
                binding.btnAddAddress.visibility = View.GONE
                binding.progress.visibility = View.VISIBLE

                val address = Address(
                    System.currentTimeMillis().toString(),
                    binding.edtCity.text.toString(),
                    binding.edtPostal.text.toString(),
                    binding.edtStreetName.text.toString(),
                    binding.edtBuild.text.toString(),
                    binding.edtApartment.text.toString(),
                    ""
                )
                viewModel.addNewAddress(address)
                dialog?.dismiss()
            } else {
                Toast.makeText(requireContext(), "please fill all required data", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    private fun isViewsNotEmpty(): Boolean {
        return (binding.edtCity.text?.isNotEmpty()!!
                && binding.edtPostal.text?.isNotEmpty()!!
                && binding.edtStreetName.text?.isNotEmpty()!!
                && binding.edtBuild.text?.isNotEmpty()!!
                && binding.edtApartment.text?.isNotEmpty()!!)
    }

    override fun onStart() {
        super.onStart()
        // set size of width to dialog to customize it with different screens
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.85).toInt()

        dialog!!.window?.setLayout(width, height)

        dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}