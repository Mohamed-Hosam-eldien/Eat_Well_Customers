package com.codingtester.eatwell.view.address

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.codingtester.eatwell.R
import com.codingtester.eatwell.model.pojo.Address

class AddressAdapter(
    private var address: MutableList<Address>,
    private val addressClick: ClickToAddress
): RecyclerView.Adapter<AddressAdapter.ViewHolder>() {

    private lateinit var viewHolder: ViewHolder

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.address_layout,
                    parent,
                    false
                )
        )
    }

    override fun getItemCount(): Int {
        return address.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        viewHolder = holder
        val currentAddress = address[position]

        holder.txtStreetAndBuildNum.text = "${currentAddress.streetName}, ${currentAddress.buildNumber}"
        holder.txtPostalAndCity.text = "${currentAddress.postalCode}, ${currentAddress.city}"
        holder.txtApartment.text = "apartment number ${currentAddress.floor}"

        if(currentAddress.details.isNotEmpty()) {
            holder.txtDetails.visibility = View.VISIBLE
            holder.txtDetails.text = currentAddress.details
        } else {
            holder.txtDetails.visibility = View.GONE
        }

        holder.imgDelete.setOnClickListener {
            addressClick.clickToDeleteAddress(currentAddress)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtStreetAndBuildNum: TextView = itemView.findViewById(R.id.txtStreetAndBuild)
        val txtPostalAndCity: TextView = itemView.findViewById(R.id.txtPostalAndCity)
        val txtApartment: TextView = itemView.findViewById(R.id.txtApartmentNum)
        val txtDetails: TextView = itemView.findViewById(R.id.txtDetails)
        val imgDelete: ImageView = itemView.findViewById(R.id.imgAddressDelete)
    }

    fun setList(newAddresses: MutableList<Address>) {
        this.address = newAddresses
        notifyDataSetChanged()
    }

    fun updateAfterRemovedItem() {
        address.removeAt(viewHolder.adapterPosition)
        notifyItemRemoved(viewHolder.adapterPosition)
        notifyItemRangeChanged(viewHolder.adapterPosition,address.size)
    }
}