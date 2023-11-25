package com.codingtester.eatwell.view.profile

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.codingtester.eatwell.view.splash.SplashActivity
import com.codingtester.eatwell.databinding.FragmentProfileBinding
import com.codingtester.eatwell.utils.Constants
import com.codingtester.eatwell.view.address.AddressActivity
import com.codingtester.eatwell.view.register.WelcomeActivity
import com.codingtester.eatwell.viewmodel.DataViewModel
import com.codingtester.eatwell.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val registerViewModel by viewModels<RegisterViewModel>()
    private val dataViewModel by viewModels<DataViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        registerViewModel.loginLiveData.observe(viewLifecycleOwner) { response ->
            if(response?.firebaseUser != null) {
                binding.dataLayout.visibility = View.VISIBLE
                binding.notRegisterLayout.visibility = View.GONE
                binding.txtProfileName.text = Constants.currentCustomer.name
                binding.txtProfileEmail.text = Constants.currentCustomer.email
                binding.txtProfilePhone.text = Constants.currentCustomer.phoneNumber
                setTotalPoints()
            } else {
                binding.dataLayout.visibility = View.GONE
                binding.notRegisterLayout.visibility = View.VISIBLE
            }
        }

        binding.btnSign.setOnClickListener {
            startActivity(Intent(requireContext(), WelcomeActivity::class.java))
        }

        binding.cardAddress.setOnClickListener {
            startActivity(Intent(requireContext(), AddressActivity::class.java))
        }

        binding.cardSignOut.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Logout!")
                .setMessage("Are you sure to logout?")
                .setPositiveButton("Logout") { _, _ ->
                    registerViewModel.logout()
                    startActivity(Intent(requireContext(), SplashActivity::class.java))
                    requireActivity().finish()
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    private fun setTotalPoints() {
        dataViewModel.getUserPoints()
        dataViewModel.getUserPointsLiveData.observe(requireActivity()) { points ->
            binding.txtProfilePoints.text = "$points Points"
        }
    }
}