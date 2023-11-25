package com.codingtester.eatwell.utils

import android.util.Log
import com.codingtester.eatwell.model.pojo.Customer
import com.codingtester.eatwell.utils.notification.PushNotification
import com.codingtester.eatwell.utils.notification.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

object Constants {
    lateinit var currentCustomer: Customer

    const val BASE_URL = "https://fcm.googleapis.com"
    const val SERVER_KEY = "AAAA5uPBuoU:APA91bGzTMN7ZdaGKqxHDVbHpy_U4VghGof_LSoZM2MK0s2EDX-v6DyV7cEa2k_AXkLSHLica60aoEnOt3zEvBTQXzkujZgz-3SOebFXuShdLMYmDdJz1goaqy5X1Xqymqit5aHAj7Fm"
    const val CONTENT_TYPE = "application/json"

    fun sendNotification(
        notification: PushNotification
    ) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = RetrofitInstance.api.postNotification(notification)
            if (response.isSuccessful) {
                Log.e("TAG", "Success")
            } else {
                Log.e("TAG", "Failed")
            }
        } catch (e: Exception) {
            Log.e("TAG", "sendNotification: " + e.message)
        }
    }
}