package com.codingtester.eatwell.view.reward.subscribe

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.codingtester.eatwell.databinding.SubscribeBottomDialogBinding
import com.codingtester.eatwell.view.splash.SplashActivity
import com.codingtester.eatwell.viewmodel.DataViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SubscribeDialog : BottomSheetDialogFragment() {

    private lateinit var binding: SubscribeBottomDialogBinding
    private val viewModel: DataViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SubscribeBottomDialogBinding.inflate(layoutInflater)

        val title = arguments?.getString("title")

        viewModel.addSubscribeItemsLiveData.observe(this) {
            if(it == "Success") {
                val intent = Intent(requireContext(), SplashActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                dialog?.dismiss()
            }
        }

        binding.btnProced.setOnClickListener {
            viewModel.addSubscribeItem(title!!)
        }

        return binding.root
    }
}