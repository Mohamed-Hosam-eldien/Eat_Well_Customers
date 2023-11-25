package com.codingtester.eatwell.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codingtester.eatwell.model.pojo.Customer
import com.codingtester.eatwell.model.pojo.UserResponse
import com.codingtester.eatwell.model.repo.RegisterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val mainRepository: RegisterRepository
) : ViewModel() {

    private val mutableLogin: MutableLiveData<UserResponse?> = MutableLiveData()
    val loginLiveData: LiveData<UserResponse?> = mutableLogin

    private val mutableSignup: MutableLiveData<UserResponse?> = MutableLiveData()
    val signupLiveData: LiveData<UserResponse?> = mutableSignup

    private val customer: MutableLiveData<Customer> = MutableLiveData()
    val customerLiveData: LiveData<Customer> = customer

    init {
        if (mainRepository.currentUser != null) {
            mutableLogin.value = UserResponse(mainRepository.currentUser, "")
        } else {
            mutableLogin.value = UserResponse(null, "")
        }
    }

    fun login(email: String, pass: String) = viewModelScope.launch {
        val user = mainRepository.login(email, pass)
        mutableLogin.value = user
    }

    fun signup(name: String, email: String, pass: String) = viewModelScope.launch {
        val user = mainRepository.signup(name, email, pass)
        mutableSignup.value = user
    }

    fun logout() {
        mainRepository.logout()
        mutableLogin.value = null
        mutableSignup.value = null
    }

    fun addUserToFirebase(customer: Customer) {
        mainRepository.addUserToFirebase(customer)
    }

    fun getCustomerData(userId: String) {
        mainRepository.getUserData(userId, customer)
    }
}