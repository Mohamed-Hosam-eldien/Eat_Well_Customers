package com.codingtester.eatwell.view.details


import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.codingtester.eatwell.R
import com.codingtester.eatwell.databinding.SignupLayoutBinding
import com.codingtester.eatwell.view.register.WelcomeActivity
import com.codingtester.eatwell.viewmodel.DataViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupDialog : DialogFragment() {

    private lateinit var binding: SignupLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = SignupLayoutBinding.inflate(layoutInflater)

        binding.btnSign.setOnClickListener {
            startActivity(Intent(requireContext(), WelcomeActivity::class.java))
            dismiss()
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        // set size of width to dialog to customize it with different screens
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.85).toInt()

        dialog!!.window?.setLayout(width, height)

        dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}