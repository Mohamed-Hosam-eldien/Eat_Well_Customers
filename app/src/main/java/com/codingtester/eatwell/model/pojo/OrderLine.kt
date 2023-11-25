package com.codingtester.eatwell.model.pojo

data class OrderLine(
    val productId: String = "",
    val productName: String = "",
    val productSize: String = "",
    val productImage: String = "",
    val quantity: String = "",
    val productPrice: Int = 0,
    val totalPrice: Int = 0,
)