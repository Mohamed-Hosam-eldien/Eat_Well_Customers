package com.codingtester.eatwell.view.reserve

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.codingtester.eatwell.databinding.FragmentReserveBinding
import com.codingtester.eatwell.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReserveFragment : Fragment() {

    private lateinit var binding: FragmentReserveBinding
    private val registerViewModel by viewModels<RegisterViewModel>()
    private var isUserLogin = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReserveBinding.inflate(layoutInflater)

        registerViewModel.loginLiveData.observe(viewLifecycleOwner) { response ->
            if(response?.firebaseUser != null) {
                isUserLogin = true
            }
        }

        binding.btnReserveNow.setOnClickListener {
            if(isUserLogin) {
                startActivity(Intent(requireContext(), ReserveActivity::class.java))
            } else {
                ReserveDialog().show(requireActivity().supportFragmentManager, "sup")
            }
        }

        return binding.root
    }
}