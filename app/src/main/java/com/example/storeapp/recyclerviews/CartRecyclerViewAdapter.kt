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
import com.example.storeapp.database.ItemViewModel
import com.example.storeapp.fragments.CartFragment
import com.example.storeapp.objects.Item

class CartRecyclerViewAdapter(private val itemList: ArrayList<Item>
                              , private val itemListener: ItemListener
                              , private val viewModel: ItemViewModel
): RecyclerView.Adapter<CartRecyclerViewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.item_row,parent,false)
        return ViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = itemList[position]

        holder.name.text = " [ ${currentItem.amount} ]" + currentItem.name
        holder.price.text = currentItem.price.toString()

        val manager: RequestManager? = holder.img?.let { Glide.with(it) }
        manager?.load(currentItem.imgUrl)?.thumbnail(0.05f)?.transition(
            DrawableTransitionOptions.withCrossFade()
        )?.into(holder.img as ImageView)

        holder.itemView.setOnClickListener{
            val item = itemList.get(position)
           // itemListener.itemClicked(item)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }



    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val name: TextView = itemView.findViewById(R.id.itemName)
        val price: TextView = itemView.findViewById(R.id.itemPrice)
        val img: ImageView = itemView.findViewById(R.id.itemImg)

    }


    fun deleteItem(position: Int) {
        val item = itemList[position]
        viewModel.deleteItem(item)
        notifyItemChanged(position)
    }

    fun setList(items:List<Item>){
        itemList.clear()
        itemList.addAll(items)
    }




}