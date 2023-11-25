package com.codingtester.eatwell.view.reward.points.pointrequest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.codingtester.eatwell.databinding.ActivityRequestPointsBinding
import com.codingtester.eatwell.model.pojo.Point
import com.codingtester.eatwell.viewmodel.DataViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RequestPointsActivity : AppCompatActivity() {

    private val viewModel: DataViewModel by viewModels()
    private lateinit var binding: ActivityRequestPointsBinding
    private val pointAdapter by lazy { RequestPointAdapter(mutableListOf()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRequestPointsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recycler.apply {
            layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        }

        viewModel.getPointRequest()
        viewModel.getPointsRequestsLiveData.observe(this) { requests ->
            if(requests.isNotEmpty()) {
                pointAdapter.setList(requests as MutableList<Point>)
                binding.recycler.adapter = pointAdapter
                binding.progress.visibility = View.GONE
                binding.imgEmpty.visibility = View.GONE
            } else {
                binding.imgEmpty.visibility = View.VISIBLE
            }
        }

        binding.imgBack.setOnClickListener { finish() }
    }
}