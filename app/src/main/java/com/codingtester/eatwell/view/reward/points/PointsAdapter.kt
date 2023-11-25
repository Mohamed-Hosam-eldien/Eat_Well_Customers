package com.codingtester.eatwell.view.reward.points

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codingtester.eatwell.R
import com.codingtester.eatwell.model.pojo.Point

class PointsAdapter(
    private var pointList: MutableList<Point>,
    private val onClickToPoints: OnClickToPoints
): RecyclerView.Adapter<PointsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.point_layout,
                    parent,
                    false
                )
        )
    }

    override fun getItemCount(): Int {
        return pointList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val point = pointList[position]

        Glide.with(holder.itemView.context)
            .load(point.image)
            .into(holder.pointImage)

        holder.pointTitle.text = point.title

        holder.totalPoint.text = "${point.totalPoints} Points"

        holder.btnActive.setOnClickListener {
            onClickToPoints.onActive(point)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val pointImage: ImageView = itemView.findViewById(R.id.imgPoint)
        val pointTitle: TextView = itemView.findViewById(R.id.txtPointsTitle)
        val totalPoint: TextView = itemView.findViewById(R.id.txtPointsTotal)
        val btnActive: Button = itemView.findViewById(R.id.btnActivePoints)
    }

    fun setList(newList: MutableList<Point>) {
        this.pointList = newList
        notifyDataSetChanged()
    }
}