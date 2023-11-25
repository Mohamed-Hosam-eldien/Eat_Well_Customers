package com.codingtester.eatwell.view.reward.points

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.codingtester.eatwell.databinding.PointRequestDialogBinding
import com.codingtester.eatwell.model.pojo.Point
import com.codingtester.eatwell.viewmodel.DataViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PointRequestDialog : DialogFragment() {

    private lateinit var binding: PointRequestDialogBinding
    private val viewModel: DataViewModel by viewModels()
    private lateinit var point: Point

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PointRequestDialogBinding.inflate(layoutInflater)

        point = arguments?.getSerializable("point") as Point

        Glide.with(requireContext())
            .load(point.image)
            .into(binding.img)

        binding.btnSend.setOnClickListener {
            sendPoints()
        }

        binding.txtPointsMinus.text = "- ${point.totalPoints}"

        viewModel.sendPointsRequestLiveData.observe(this) { response ->
            if(response == "Success") {
                Toast.makeText(requireContext(), "Points request has been send successfully", Toast.LENGTH_SHORT).show()
                viewModel.getUserPoints()
                viewModel.calcPointsAfterSendRewards(point.totalPoints)
                dialog?.dismiss()
            } else {
                Toast.makeText(requireContext(), "Error, try again", Toast.LENGTH_SHORT).show()
                binding.btnSend.visibility = View.VISIBLE
                binding.progress.visibility = View.GONE
            }
        }

        return binding.root
    }

    private fun sendPoints() {
        binding.btnSend.visibility = View.GONE
        binding.progress.visibility = View.VISIBLE
        viewModel.sendPointRequest(point)
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