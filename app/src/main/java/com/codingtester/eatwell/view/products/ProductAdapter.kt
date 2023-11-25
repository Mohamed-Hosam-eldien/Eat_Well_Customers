package com.codingtester.eatwell.view.products

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

class ProductAdapter(
    private var products: List<Product>,
    private val product: ClickToProduct
): RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.product_layout,
                    parent,
                    false
                )
        )
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentProduct = products[position]

        Glide.with(holder.itemView.context)
            .load(currentProduct.image)
            .into(holder.productImage)

        holder.productName.text = currentProduct.name
        holder.productPrice.text = currentProduct.price?.medium.toString()
        holder.productRating.rating = currentProduct.rate.toFloat()

        holder.itemView.setOnClickListener {
            product.onClickToProduct(currentProduct)
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