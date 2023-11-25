package com.codingtester.eatwell.view.checkout

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.codingtester.eatwell.databinding.PointReviewDialogBinding
import com.codingtester.eatwell.model.pojo.Order
import com.codingtester.eatwell.utils.Constants
import com.codingtester.eatwell.utils.notification.NotificationData
import com.codingtester.eatwell.utils.notification.PushNotification
import com.codingtester.eatwell.utils.observeOnce
import com.codingtester.eatwell.view.main.MainActivity
import com.codingtester.eatwell.viewmodel.DataViewModel
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PointReviewDialog : DialogFragment() {

    private lateinit var binding: PointReviewDialogBinding
    private val viewModel: DataViewModel by viewModels()
    private var total = ""
    private var takeState = ""
    private var address = ""
    private var points = "0"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PointReviewDialogBinding.inflate(layoutInflater)

        total = arguments?.getString("total")!!
        takeState = arguments?.getString("takeState")!!
        address = arguments?.getString("address")!!

        when(total.toInt()) {
            in (5..49) -> {
                points = "2"
            }

            in (50..150) -> {
                points = "6"
            }

            in (151..350) -> {
                points = "12"
            }

            in (351..600) -> {
                points = "20"
            }

            in (601..1000) -> {
                points = "33"
            }

            in (1001..3000) -> {
                points = "45"
            }

            in (3001..7000) -> {
                points = "70"
            }
        }

        binding.textTotalPoints.text = points

        viewModel.sendOrderLiveData.observe(this) { state ->
            if(state == "Success") {
                viewModel.clearCart()
                viewModel.calcPointsAfterSendOrder(points)
                val intent = Intent(requireContext(), MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                Toast.makeText(requireContext(), "Order has been sent successfully", Toast.LENGTH_SHORT).show()
            } else {
                binding.btnSend.visibility = View.VISIBLE
                binding.progress.visibility = View.GONE
                Toast.makeText(requireContext(), "Failed to send order, try again", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnSend.setOnClickListener {
            sendOrder()
        }

        viewModel.getAdminsTokenLiveData.observe(requireActivity()) { list ->
            list.forEach {
                sendOrderNotification(it)
            }
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        // set size of width to dialog to customize it with different screens
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.85).toInt()

        dialog!!.window?.setLayout(width, height)

        dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun sendOrder() {
        binding.btnSend.visibility = View.GONE
        binding.progress.visibility = View.VISIBLE

        viewModel.getCart()
        viewModel.cartLiveData.observeOnce(this) { products ->
            val order = Order(
                System.currentTimeMillis().toString(),
                System.currentTimeMillis().toString(),
                "In Review",
                products,
                total,
                "0",
                "none",
                Constants.currentCustomer,
                takeState == "Deliver the order to my address",
                address
            )
            viewModel.sendOrder(order)
            viewModel.getAllAdminTokens()
        }
    }

    private fun sendOrderNotification(token: String) {
        val pushNotification = PushNotification(
            NotificationData("New Order", "New Order Received.."),
            token
        )
        Constants.sendNotification(pushNotification)
    }

}