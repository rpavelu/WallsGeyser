package com.ratushny.wallsgeyser

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ratushny.wallsgeyser.data.WallsDto

class WallsListAdapter : RecyclerView.Adapter<WallsListAdapter.ViewHolder>() {

    var wallsData: List<WallsDto> = emptyList()
        private set
    lateinit var mClickListener: ClickListener

    fun setOnItemClickListener(aClickListener: ClickListener) {
        mClickListener = aClickListener
    }

    interface ClickListener {
        fun onClick(pos: Int)
    }

    fun addWalls(wallsList: List<WallsDto>) {
        wallsData = wallsList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_wall, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val wall: WallsDto = wallsData[position]

        Glide.with(holder.itemView.context)
            .load(wall.previewURL)
            .into(holder.wallsImageView)
    }

    override fun getItemCount() = wallsData.size

    inner class ViewHolder(
        itemView: View,
        val wallsImageView: ImageView = itemView.findViewById(R.id.wallImageView)
    ) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        override fun onClick(v: View) {
            mClickListener.onClick(adapterPosition)
        }

        init {
            itemView.setOnClickListener(this)
        }
    }
}