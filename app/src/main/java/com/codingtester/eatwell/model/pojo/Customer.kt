package com.codingtester.eatwell.model.pojo

data class Customer(
    val id: String? = "",
    val name: String? = "",
    val email: String? = "",
    val phoneNumber: String = "",
    var points: String = "0",
    @field:JvmField
    val isSubscribe: Boolean = false,
    val subscribeModel: String? = "",
    val subscribeDate: String? = "",
    var token: String? = ""
)
