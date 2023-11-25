package com.codingtester.eatwell.view.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.viewModels
import com.codingtester.eatwell.R
import com.codingtester.eatwell.model.pojo.Customer
import com.codingtester.eatwell.utils.Constants
import com.codingtester.eatwell.view.main.MainActivity
import com.codingtester.eatwell.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private val viewModel by viewModels<RegisterViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        checkIfUserLogin()
    }

    private fun checkIfUserLogin() {
        viewModel.loginLiveData.observe(this) { response ->
            if(response?.firebaseUser != null) {
                viewModel.getCustomerData(response.firebaseUser!!.uid)
                viewModel.customerLiveData.observe(this) { customer ->
                    if(customer != null) {
                        Constants.currentCustomer = Customer(
                            customer.id,
                            customer.name,
                            customer.email,
                            customer.phoneNumber,
                            customer.points,
                            customer.isSubscribe,
                            customer.subscribeModel,
                            customer.subscribeDate,
                            customer.token
                        )
                        gotoHomeActivity()
                    }
                }
            } else {
                gotoHomeActivity()
            }
        }
    }

    private fun gotoHomeActivity() {
        Handler(Looper.myLooper()!!).postDelayed( {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 1000)
    }
}