package com.codingtester.eatwell.model.pojo

import com.google.firebase.auth.FirebaseUser

data class UserResponse(
    var firebaseUser: FirebaseUser?,
    val errorMessage: String?)