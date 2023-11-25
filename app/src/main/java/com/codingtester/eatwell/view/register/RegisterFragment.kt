package com.codingtester.eatwell.view.register

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.codingtester.eatwell.R
import com.codingtester.eatwell.databinding.FragmentRegisterBinding
import com.codingtester.eatwell.model.pojo.Customer
import com.codingtester.eatwell.utils.Constants
import com.codingtester.eatwell.utils.await
import com.codingtester.eatwell.utils.notification.FirebaseService
import com.codingtester.eatwell.view.splash.SplashActivity
import com.codingtester.eatwell.viewmodel.RegisterViewModel
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private val viewModel by viewModels<RegisterViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val fragView = inflater.inflate(R.layout.fragment_register, container, false)
        binding = FragmentRegisterBinding.bind(fragView)

        binding.btnCreateAccount.setOnClickListener {
            signUpNewUser()
        }

        viewModel.signupLiveData.observe(viewLifecycleOwner) { response ->
            if (response?.firebaseUser != null) {
                Constants.currentCustomer = Customer(
                    response.firebaseUser!!.uid,
                    response.firebaseUser!!.displayName,
                    response.firebaseUser!!.email,
                    binding.edtPhone.text.toString(),
                    "0",
                    false,
                    null
                )

                lifecycleScope.launch {
                    Constants.currentCustomer.token = getAdminToken()
                    viewModel.addUserToFirebase(Constants.currentCustomer)

                    val intent = Intent(requireActivity(), SplashActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    requireActivity().startActivity(intent)
                }

            } else {
                binding.btnCreateAccount.visibility = View.VISIBLE
                binding.progress.visibility = View.GONE
                Toast.makeText(requireContext(), response?.errorMessage, Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    private fun signUpNewUser() {
        val name = binding.edtName.text.toString()
        val email = binding.edtEmail.text.toString()
        val pass = binding.edtPass.text.toString()

        if (email.isNotEmpty() && pass.isNotEmpty() && name.isNotEmpty()) {
            binding.btnCreateAccount.visibility = View.GONE
            binding.progress.visibility = View.VISIBLE
            try {
                lifecycleScope.launch {
                    viewModel.signup(name, email, pass)
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(requireContext(), "please fill all data", Toast.LENGTH_SHORT).show()
        }
    }

    private suspend fun getAdminToken(): String {
        var adminToken = ""
        FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
            FirebaseService.token = token
            adminToken = token
        }.await()
        return adminToken
    }
}