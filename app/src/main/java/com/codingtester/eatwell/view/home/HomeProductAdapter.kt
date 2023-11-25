package com.codingtester.eatwell.view.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codingtester.eatwell.R
import com.codingtester.eatwell.model.pojo.Product
import com.codingtester.eatwell.view.products.ClickToProduct

class HomeProductAdapter(
    private var products: List<Product>,
    private val product: ClickToProduct
): RecyclerView.Adapter<HomeProductAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.home_product_layout,
                    parent,
                    false
                )
        )
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.itemView.context)
            .load(products[position].image)
            .into(holder.productImage)

        holder.productName.text = products[position].name
        holder.productPrice.text = products[position].price?.medium.toString()
        holder.productRating.rating = products[position].rate.toFloat()

        holder.itemView.setOnClickListener {
            product.onClickToProduct(products[position])
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImage: ImageView = itemView.findViewById(R.id.imgPop)
        val productName: TextView = itemView.findViewById(R.id.txtPopName)
        val productPrice: TextView = itemView.findViewById(R.id.txtPopPrice)
        val productRating: RatingBar = itemView.findViewById(R.id.popRating)
    }

    fun setList(newCategories: List<Product>) {
        this.products = newCategories
        notifyDataSetChanged()
    }
}