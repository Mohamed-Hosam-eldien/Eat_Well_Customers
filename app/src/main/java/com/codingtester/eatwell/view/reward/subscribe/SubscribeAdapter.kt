package com.codingtester.eatwell.view.reward.subscribe

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.codingtester.eatwell.R
import com.codingtester.eatwell.model.pojo.Point
import com.codingtester.eatwell.model.pojo.Subscribe
import com.codingtester.eatwell.utils.Constants

class SubscribeAdapter(
    private var subscribe: MutableList<Subscribe>,
    private val onClickToSubscribe: OnClickToSubscribe
): RecyclerView.Adapter<SubscribeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.subscribe_item,
                    parent,
                    false
                )
        )
    }

    override fun getItemCount(): Int {
        return subscribe.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sub = subscribe[position]
        holder.txtPrice.text = "${sub.price} DKK"
        holder.txtDiscount.text = "${sub.percentageDiscount}%"
        holder.txtTitle.text = sub.title

        when(sub.title) {
            "Basic" -> {
                holder.cardView.setCardBackgroundColor(holder.itemView.context.getColor(R.color.basic_color))
            }

            "Premium" -> {
                holder.cardView.setCardBackgroundColor(holder.itemView.context.getColor(R.color.premium_color))
            }

            "Exclusive" -> {
                holder.cardView.setCardBackgroundColor(holder.itemView.context.getColor(R.color.exclusive_color))
            }
        }

        if(Constants.currentCustomer.subscribeModel == sub.title) {
            holder.btnSub.visibility = View.GONE
        }

        holder.btnSub.setOnClickListener {
            onClickToSubscribe.onSubscribe(sub)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView: CardView = itemView.findViewById(R.id.cardSub)
        val txtTitle: TextView = itemView.findViewById(R.id.txtSubTitle)
        val txtPrice: TextView = itemView.findViewById(R.id.txtSubPrice)
        val txtDiscount: TextView = itemView.findViewById(R.id.txtSubDesc)
        val btnSub: Button = itemView.findViewById(R.id.btnSub)
    }

    fun setList(newList: MutableList<Subscribe>) {
        this.subscribe = newList
        notifyDataSetChanged()
    }
}