package com.codingtester.eatwell.view.category

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codingtester.eatwell.R
import com.codingtester.eatwell.model.pojo.Category
import com.codingtester.eatwell.view.products.ProductsActivity

class CategoryAdapter(private var categories: List<Category>): RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.category_layout,
                    parent,
                    false
                )
        )
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context

        Glide.with(context)
            .load(categories[position].image)
            .into(holder.categoryImage)

        holder.categoryName.text = categories[position].name

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ProductsActivity::class.java)
            intent.putExtra("category", categories[position].name)
            context.startActivity(intent)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryImage: ImageView = itemView.findViewById(R.id.imgCategory)
        val categoryName: TextView = itemView.findViewById(R.id.txtCategory)
    }

    fun setList(newCategories: List<Category>) {
        this.categories = newCategories
        notifyDataSetChanged()
    }

}