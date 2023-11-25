package com.codingtester.eatwell.view.reserve

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.codingtester.eatwell.databinding.ActivityReverseBinding
import com.codingtester.eatwell.model.pojo.Reserve
import com.codingtester.eatwell.utils.Constants
import com.codingtester.eatwell.utils.notification.NotificationData
import com.codingtester.eatwell.utils.notification.PushNotification
import com.codingtester.eatwell.viewmodel.DataViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class ReserveActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReverseBinding
    private val dataViewModel by viewModels<DataViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReverseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnIncrease.setOnClickListener {
            val oldValue = binding.txtCount.text.toString().toInt() + 1
            if(oldValue <= 10) {
                binding.txtCount.text = oldValue.toString()
            }
        }

        binding.btnDecrease.setOnClickListener {
            val oldValue = binding.txtCount.text.toString().toInt() - 1
            if(oldValue >=1 ) {
                binding.txtCount.text = oldValue.toString()
            }
        }

        dataViewModel.setReserveLiveData.observe(this) { state ->
            if(state == "Success") {
                binding.progress.visibility = View.GONE
                binding.btnReserve.visibility = View.VISIBLE
                binding.edtDate.setText("")
                binding.edtTimeTo.setText("")
                binding.edtTimeFrom.setText("")
                binding.txtCount.text = "1"
                Toast.makeText(this, "Reserve Send Successfully", Toast.LENGTH_SHORT).show()
            } else {
                binding.progress.visibility = View.GONE
                binding.btnReserve.visibility = View.VISIBLE
                Toast.makeText(this, "error, try again!!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnReserve.setOnClickListener {
            binding.progress.visibility = View.VISIBLE
            binding.btnReserve.visibility = View.GONE

            val reserve = Reserve(
                System.currentTimeMillis().toString(),
                binding.txtCount.text.toString().toInt(),
                "",
                binding.edtDate.text.toString(),
                binding.edtTimeFrom.text.toString(),
                binding.edtTimeTo.text.toString(),
                Constants.currentCustomer,
                "inReview"
            )

            dataViewModel.getAdminsTokenLiveData.observe(this) { list ->
                list.forEach {
                    sendOrderNotification(it)
                }
            }

            dataViewModel.sendReserveToServer(reserve)
            dataViewModel.getAllAdminTokens()
        }

        binding.edtDate.setOnClickListener {
            setCurrentDate()
        }

        binding.edtTimeFrom.setOnClickListener {
            setCurrentTime(binding.edtTimeFrom)
        }

        binding.edtTimeTo.setOnClickListener {
            setCurrentTime(binding.edtTimeTo)
        }
    }

    private fun setCurrentTime(edt: TextView) {
        val c = Calendar.getInstance()
        val mHour = c[Calendar.HOUR_OF_DAY]
        val mMinute = c[Calendar.MINUTE]

        // Launch Time Picker Dialog

        // Launch Time Picker Dialog
        val timePickerDialog = TimePickerDialog(this, { _, hourOfDay, minute ->
            edt.setText("$hourOfDay:$minute") },
            mHour,
            mMinute,
            false
        )
        timePickerDialog.show()
    }

    private fun setCurrentDate() {
        val c: Calendar = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


        val datePickerDialog = DatePickerDialog(this, { _, cYear, monthOfYear, dayOfMonth ->
            binding.edtDate.setText(dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + cYear) },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    private fun sendOrderNotification(token: String) {
        val pushNotification = PushNotification(
            NotificationData("Reservation Update", "New Reservation Received"),
            token
        )
        Constants.sendNotification(pushNotification)
    }
}