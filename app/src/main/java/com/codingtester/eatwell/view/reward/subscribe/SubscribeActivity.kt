package com.codingtester.eatwell.view.reward.subscribe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.codingtester.eatwell.databinding.ActivitySubscribeBinding
import com.codingtester.eatwell.model.pojo.Subscribe
import com.codingtester.eatwell.utils.Constants
import com.codingtester.eatwell.viewmodel.DataViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SubscribeActivity : AppCompatActivity(), OnClickToSubscribe {

    private lateinit var binding: ActivitySubscribeBinding
    private val viewModel: DataViewModel by viewModels()
    private val subAdapter by lazy { SubscribeAdapter(mutableListOf(), this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubscribeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recycler.apply {
            layoutManager = LinearLayoutManager(this@SubscribeActivity, LinearLayoutManager.HORIZONTAL, false)
        }

        viewModel.getSubscribeItems()
        viewModel.getSubscribeItemsLiveData.observe(this) { subs ->
            subAdapter.setList(subs.toMutableList())
            binding.recycler.adapter = subAdapter
        }
    }

    override fun onSubscribe(sub: Subscribe) {
        val bundle = Bundle()
        bundle.putString("title", sub.title)

        val dialog = SubscribeDialog()
        dialog.arguments = bundle
        dialog.show(supportFragmentManager, "")
    }

    override fun onResume() {
        super.onResume()
        viewModel.getSubscribeItems()
    }
}