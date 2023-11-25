package com.codingtester.eatwell.view.orders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.codingtester.eatwell.R
import com.codingtester.eatwell.model.pojo.Order
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class OrderAdapter(
    private var orders: MutableList<Order>
): RecyclerView.Adapter<OrderAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.order_layout,
                    parent,
                    false
                )
        )
    }

    override fun getItemCount(): Int {
        return orders.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val order = orders[position]
        holder.txtId.text = order.id

        holder.txtPrepTime.text = order.preparationTime
        holder.txtPrice.text = "${order.totalPrice} DKK"
        holder.txtAddress.text = order.orderAddress
        holder.txtDate.text = convertMilleSecondsToDate(order.date)

        when(order.status) {
            "In Review" -> {
                holder.txtStatus.text = "In-Review"
                holder.imgOrder.setImageResource(R.drawable.in_review)
                holder.preLayout.visibility = View.GONE
            }
            "preparing" -> {
                holder.txtStatus.text = "Preparing"
                holder.imgOrder.setImageResource(R.drawable.in_progress)
                holder.preLayout.visibility = View.VISIBLE
            }
            "complete" -> {
                holder.txtStatus.text = "Complete"
                holder.preLayout.visibility = View.GONE
                holder.imgOrder.setImageResource(R.drawable.done)
            }
            "inTheWay" -> {
                holder.txtStatus.text = "In the way"
                holder.preLayout.visibility = View.GONE
                holder.imgOrder.setImageResource(R.drawable.delivery)
            }
            "rejected" -> {
                holder.txtStatus.text = "Rejected"
                holder.preLayout.visibility = View.GONE
                holder.imgOrder.setImageResource(R.drawable.rejected)
            }
        }
    }

    private fun convertMilleSecondsToDate(date: String): String? {
        val formatter = SimpleDateFormat("dd/MM/yyyy hh:mm", Locale.getDefault())
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = date.toLong()
        return formatter.format(calendar.time)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgOrder: ImageView = itemView.findViewById(R.id.imgOrder)
        val txtId: TextView = itemView.findViewById(R.id.txtOrderId)
        val txtPrice: TextView = itemView.findViewById(R.id.txtPrice)
        val txtAddress: TextView = itemView.findViewById(R.id.txtAddress)
        val txtDate: TextView = itemView.findViewById(R.id.txtDate)
        val txtPrepTime: TextView = itemView.findViewById(R.id.txtPreprationTime)
        val txtStatus: TextView = itemView.findViewById(R.id.txtState)
        val preLayout: LinearLayout = itemView.findViewById(R.id.layoutPrepration)
    }

    fun setList(newList: MutableList<Order>) {
        this.orders = newList
        notifyDataSetChanged()
    }
}