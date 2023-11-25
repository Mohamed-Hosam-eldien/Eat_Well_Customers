package com.codingtester.eatwell.view.cart

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codingtester.eatwell.R
import com.codingtester.eatwell.model.pojo.OrderLine

class CartAdapter(
    private var orderLines: MutableList<OrderLine>,
    private var onClickToDeleteCart: OnClickToDeleteCart
): RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    private lateinit var viewHolder: ViewHolder

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.cart_layout,
                    parent,
                    false
                )
        )
    }

    override fun getItemCount(): Int {
        return orderLines.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        viewHolder = holder
        val orderLines = orderLines[position]

        Glide.with(holder.itemView.context)
            .load(orderLines.productImage)
            .into(holder.productImage)

        holder.productName.text = orderLines.productName
        holder.productPrice.text = orderLines.totalPrice.toString()
        holder.productSize.text = orderLines.productSize
        holder.productQuantity.text = "(${orderLines.quantity})"

        holder.imgClose.setOnClickListener {
            onClickToDeleteCart.onDelete(orderLines.productId)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImage: ImageView = itemView.findViewById(R.id.imgCart)
        val productName: TextView = itemView.findViewById(R.id.txtCartName)
        val productPrice: TextView = itemView.findViewById(R.id.txtCartPrice)
        val productSize: TextView = itemView.findViewById(R.id.txtCartSize)
        val productQuantity: TextView = itemView.findViewById(R.id.txtCartQuantity)
        val imgClose: ImageView = itemView.findViewById(R.id.imgCartDelete)
    }

    fun setList(newList: MutableList<OrderLine>) {
        this.orderLines = newList
        notifyDataSetChanged()
    }
}