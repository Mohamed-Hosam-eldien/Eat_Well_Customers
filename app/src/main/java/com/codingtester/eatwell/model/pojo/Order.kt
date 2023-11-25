package com.codingtester.eatwell.model.pojo

data class Order(
    val id: String = "",
    val date: String = "",
    val status: String = "",
    val products: List<OrderLine> = emptyList(),
    val totalPrice: String = "",
    val discount: String = "",
    val preparationTime: String = "",
    val customer: Customer? = null,
    val isDeliveryToHome: Boolean = false,
    val orderAddress: String = "",
)