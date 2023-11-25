package com.codingtester.eatwell.view.reward.points

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.codingtester.eatwell.databinding.ActivityPointsBinding
import com.codingtester.eatwell.model.pojo.Point
import com.codingtester.eatwell.utils.Constants
import com.codingtester.eatwell.view.reward.points.pointrequest.RequestPointsActivity
import com.codingtester.eatwell.viewmodel.DataViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PointsActivity : AppCompatActivity(), OnClickToPoints {

    private val viewModel: DataViewModel by viewModels()
    private lateinit var binding: ActivityPointsBinding
    private val pointAdapter by lazy { PointsAdapter(mutableListOf(), this) }
    private var totalPoints = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPointsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recycler.apply {
            layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        }

        viewModel.getUserPoints()
        viewModel.getUserPointsLiveData.observe(this) { points ->
            binding.txtPoints.text = "Total Points : $points"
            totalPoints = points
            Constants.currentCustomer.points = points
        }

        viewModel.getPoints()
        viewModel.getPointsLiveData.observe(this) { points ->
            pointAdapter.setList(points as MutableList<Point>)
            binding.recycler.adapter = pointAdapter
            binding.progress.visibility = View.GONE
        }

        binding.imgPointsRequest.setOnClickListener {
            startActivity(Intent(this, RequestPointsActivity::class.java))
        }

        binding.imgBack.setOnClickListener { finish() }
    }

    override fun onActive(point: Point) {
        if(point.totalPoints.toInt() > totalPoints.toInt()) {
            Toast.makeText(this, "Sorry, you have not enough points for this reward", Toast.LENGTH_SHORT).show()
            return
        }

        val bundle = Bundle()
        bundle.putSerializable("point", point)

        val dialog = PointRequestDialog()
        dialog.arguments = bundle
        dialog.show(supportFragmentManager, "")
    }
}