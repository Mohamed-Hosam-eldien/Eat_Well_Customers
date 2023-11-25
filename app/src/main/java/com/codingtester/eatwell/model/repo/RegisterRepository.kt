package com.codingtester.eatwell.model.repo

import androidx.lifecycle.MutableLiveData
import com.codingtester.eatwell.model.pojo.Customer
import com.codingtester.eatwell.model.pojo.UserResponse
import com.codingtester.eatwell.utils.await
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import javax.inject.Inject

class RegisterRepository @Inject constructor (private val firebaseAuth: FirebaseAuth) {

    val currentUser: FirebaseUser? get() = firebaseAuth.currentUser

    suspend fun login(email: String, password: String): UserResponse {
        return try {
            val currentUser = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            UserResponse(currentUser.user, "")
        } catch (ex: Exception) {
            UserResponse(null, ex.message)
        }
    }

    suspend fun signup(name: String, email: String, password: String): UserResponse {
        return try {
            val newUser = firebaseAuth.createUserWithEmailAndPassword(
                email, password).await()

            val profileUpdates = userProfileChangeRequest {
                displayName = name
            }
            newUser.user?.updateProfile(profileUpdates)?.await()
            UserResponse(newUser.user, "")
        } catch (ex: Exception) {
            UserResponse(null, ex.message)
        }
    }

    fun logout() {
        firebaseAuth.signOut()
    }

    fun addUserToFirebase(customer: Customer) {
        FirebaseDatabase.getInstance()
            .getReference("Users")
            .child(customer.id.toString())
            .setValue(customer)
    }

    fun getUserData(userId: String, liveData: MutableLiveData<Customer>) {
        FirebaseDatabase.getInstance()
            .getReference("Users")
            .child(userId)
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val customer = snapshot.getValue(Customer::class.java)
                    liveData.postValue(customer)
                }
                override fun onCancelled(error: DatabaseError) {

                }
            })
    }
}