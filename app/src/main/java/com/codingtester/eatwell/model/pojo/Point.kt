package com.codingtester.eatwell.model.pojo

import java.io.Serializable

data class Point(
    val id: String = "",
    val image: String = "",
    val title: String = "",
    val description: String = "",
    val offer: String = "",
    val totalPoints: String = ""
): Serializable
