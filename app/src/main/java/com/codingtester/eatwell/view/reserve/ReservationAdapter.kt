package com.codingtester.eatwell.view.reserve

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.codingtester.eatwell.R
import com.codingtester.eatwell.model.pojo.Reserve

class ReservationAdapter(
    private var reservs: MutableList<Reserve>
): RecyclerView.Adapter<ReservationAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.reserve_item_layout,
                    parent,
                    false
                )
        )
    }

    override fun getItemCount(): Int {
        return reservs.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val reserve = reservs[position]
        holder.txtId.text = reserve.id

        holder.txtChairNumber.text = "Chairs Number: ${reserve.chairCount.toString()}"
        holder.txtDate.text = "Date: ${reserve.date}"
        holder.txtTime.text = "From  ${reserve.timeFrom}  To  ${reserve.timeTo}"

        when(reserve.state) {
            "In Review" -> {
                holder.txtStatus.text = "In-Review"
                holder.imgReserve.setImageResource(R.drawable.in_review)
            }
            "Done" -> {
                holder.txtStatus.text = "Done"
                holder.txtStatus.text = "Table Num ${reserve.tableNumber}"
                holder.txtStatus.visibility = View.VISIBLE
                holder.imgReserve.setImageResource(R.drawable.table)
            }
            "rejected" -> {
                holder.txtStatus.text = "Rejected"
                holder.imgReserve.setImageResource(R.drawable.rejected)
            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgReserve: ImageView = itemView.findViewById(R.id.imgReserve)
        val txtId: TextView = itemView.findViewById(R.id.txtId)
        val txtChairNumber: TextView = itemView.findViewById(R.id.txtChairNumber)
        val txtDate: TextView = itemView.findViewById(R.id.txtDate)
        val txtTime: TextView = itemView.findViewById(R.id.txtTime)
        val txtStatus: TextView = itemView.findViewById(R.id.txtReserveState)
    }

    fun setList(newList: MutableList<Reserve>) {
        this.reservs = newList
        notifyDataSetChanged()
    }
}