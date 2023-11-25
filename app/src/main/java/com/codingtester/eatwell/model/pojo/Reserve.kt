package com.codingtester.eatwell.model.pojo

data class Reserve(
    val id: String="",
    val chairCount: Int=0,
    val tableNumber: String="",
    val date: String="",
    val timeFrom: String="",
    val timeTo: String="",
    val customer: Customer = Customer(),
    val state: String = "",
)