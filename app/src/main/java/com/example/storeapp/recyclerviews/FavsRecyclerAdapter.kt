package com.example.storeapp.recyclerviews

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.storeapp.ItemListener
import com.example.storeapp.R
import com.example.storeapp.database.ItemDatabase
import com.example.storeapp.objects.Item

class FavsRecyclerAdapter(private val itemList: List<Item?>?,
                          private val itemListener: ItemListener

): RecyclerView.Adapter<FavsRecyclerAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.item_row,parent,false)
        return ViewHolder(listItem)
    }

    override fun getItemCount(): Int {
        return itemList?.size!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentItem = itemList?.get(position)

        holder.name.text = currentItem?.name
        holder.price.text = currentItem?.price.toString()

        val manager: RequestManager? = holder.img?.let { Glide.with(it) }
        manager?.load(currentItem?.imgUrl)?.thumbnail(0.05f)?.transition(
            DrawableTransitionOptions.withCrossFade()
        )?.into(holder.img as ImageView)

        holder.itemView.setOnClickListener{
            val item = itemList?.get(position)
            itemListener.itemClicked(item)
        }

    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val name: TextView = itemView.findViewById(R.id.itemName)
        val price: TextView = itemView.findViewById(R.id.itemPrice)
        val img: ImageView = itemView.findViewById(R.id.itemImg)

    }


}