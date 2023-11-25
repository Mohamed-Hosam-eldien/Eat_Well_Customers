package com.codingtester.eatwell.view.checkout

import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.codingtester.eatwell.R
import com.codingtester.eatwell.databinding.ActivityCheckoutBinding
import com.codingtester.eatwell.utils.Constants
import com.codingtester.eatwell.view.address.AddAddressDialog
import com.codingtester.eatwell.viewmodel.DataViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CheckoutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCheckoutBinding
    private val viewModel: DataViewModel by viewModels()
    private var paymentMethod = ""
    private var takeState = ""
    private var address = ""
    private var totalPrice = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.getStringExtra("totalPrice")?.let { price ->
            totalPrice = price

            if(Constants.currentCustomer.isSubscribe) {
                viewModel.getSubscribePercentage()
                viewModel.getSubscribePercentageLiveData.observe(this) { percentage ->          // 300
                    val totalPricePercentage = (totalPrice.toInt() * percentage.toInt()) / 100        // 45
                    val totalAfterDiscount = (totalPrice.toInt() - totalPricePercentage).toString()   // 255

                    binding.txtTotalProduct.text = totalAfterDiscount
                    binding.txtTotal.text = totalAfterDiscount

                    binding.txtTotalProductBeforeDiscount.text = "$totalPrice DKK"
                    binding.txtTotalProductBeforeDiscount.paintFlags = binding.txtTotalProductBeforeDiscount.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

                    binding.txtPercentage.text = "$percentage%"
                }
            } else {
                binding.txtTotalProduct.text = price
                binding.txtTotal.text = price
            }
        }

        binding.rgPayment.setOnCheckedChangeListener { rg, _ ->
            paymentMethod = if (rg.checkedRadioButtonId == R.id.rdbCOD) {
                "Cash on delivery"
            } else {
                "Visa"
            }
        }

        binding.rgTake.setOnCheckedChangeListener { rg, _ ->
            takeState = if (rg.checkedRadioButtonId == R.id.rdbTake) {
                "I will come to take the order"
            } else {
                "Deliver the order to my address"
            }
        }

        viewModel.getAddressData()
        viewModel.getAddressLiveData.observe(this) { addresses ->
            val addressList = mutableListOf<String>()
            addresses.forEach { address ->
                addressList.add("${address.streetName}," +
                            " ${address.buildNumber}," +
                            " ${address.postalCode}," +
                            " ${address.city}"
                )
            }

            val adapter: ArrayAdapter<String> = ArrayAdapter(
                this,
                R.layout.address_spinner_item,
                addressList
            )

            binding.autoComplete.setAdapter(adapter)
        }

        binding.autoComplete.onItemClickListener = AdapterView.OnItemClickListener { _, _, _, _ ->
            address = binding.autoComplete.text.toString()
        }

        binding.imgAdd.setOnClickListener {
            AddAddressDialog().show(supportFragmentManager, "")
        }

        binding.btnSubmitOrder.setOnClickListener {
            showPointsDialog()
        }
    }

    private fun showPointsDialog() {
        val bundle = Bundle()
        bundle.putString("total", binding.txtTotal.text.toString())
        bundle.putString("takeState", takeState)
        bundle.putString("address", address)

        val dialog = PointReviewDialog()
        dialog.arguments = bundle
        dialog.show(supportFragmentManager, "")
    }
}