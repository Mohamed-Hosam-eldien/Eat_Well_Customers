package com.codingtester.eatwell.view.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.codingtester.eatwell.databinding.FragmentHomeBinding
import com.codingtester.eatwell.model.pojo.Product
import com.codingtester.eatwell.view.cart.CartActivity
import com.codingtester.eatwell.view.details.ProductDetailsActivity
import com.codingtester.eatwell.view.products.ClickToProduct
import com.codingtester.eatwell.view.products.ProductsActivity
import com.codingtester.eatwell.view.reserve.ReservationsActivity
import com.codingtester.eatwell.view.reward.RewardsActivity
import com.codingtester.eatwell.viewmodel.DataViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(), ClickToProduct {

    private lateinit var binding: FragmentHomeBinding
    private val dessertAdapter: HomeProductAdapter by lazy { HomeProductAdapter(emptyList(), this) }
    private val popularAdapter: HomeProductAdapter by lazy { HomeProductAdapter(emptyList(), this) }
    private val viewModel: DataViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getPopularData()
        viewModel.getProductsByCategory("Dessert")

        binding.recyclerPopular.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }

        binding.recyclerDessert.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }

        viewModel.popularLiveData.observe(viewLifecycleOwner) { products ->
            popularAdapter.setList(products)
            binding.recyclerPopular.adapter = popularAdapter
        }

        viewModel.productsLiveData.observe(viewLifecycleOwner) { products ->
            dessertAdapter.setList(products.take(5))
            binding.recyclerDessert.adapter = dessertAdapter
        }

        handleShortcutsClick()

        handleSeeAllClicks()

        handleClickListener()

        binding.imgGift.setOnClickListener {
            startActivity(Intent(requireContext(), RewardsActivity::class.java))
        }

        binding.imgTable.setOnClickListener {
            startActivity(Intent(requireContext(), ReservationsActivity::class.java))
        }
    }

    private fun handleClickListener() {
        binding.imgCart.setOnClickListener {
            val intent = Intent(requireActivity(), CartActivity::class.java)
            startActivity(intent)
        }
    }

    private fun handleSeeAllClicks() {
        binding.txtDessertSeeAll.setOnClickListener {
            startActivityWithCategory("Dessert")
        }
    }

    private fun handleShortcutsClick() {
        binding.cardDrinks.setOnClickListener {
            startActivityWithCategory("Drinks")
        }

        binding.cardBurger.setOnClickListener {
            startActivityWithCategory("Burger")
        }

        binding.cardPizza.setOnClickListener {
            startActivityWithCategory("Pizza")
        }

        binding.cardTallrken.setOnClickListener {
            startActivityWithCategory("Tallerken")
        }
    }

    private fun startActivityWithCategory(category: String) {
        val intent = Intent(requireContext(), ProductsActivity::class.java)
        intent.putExtra("category", category)
        startActivity(intent)
    }

    override fun onClickToProduct(product: Product) {
        val intent = Intent(requireContext(), ProductDetailsActivity::class.java)
        intent.putExtra("product", product)
        startActivity(intent)
    }
}