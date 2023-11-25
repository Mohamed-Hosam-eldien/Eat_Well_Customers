package com.codingtester.eatwell.model.pojo

import java.io.Serializable

data class Product(
    val id: String="",
    val image: String="",
    val name: String="",
    val price: Price? = null,
    val ingredients: String="",
    val category: String="",
    val available: String="",
    val rate: Double=0.0
): Serializable
