package com.example.storeapp.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.storeapp.ItemListener
import com.example.storeapp.R
import com.example.storeapp.database.ItemDatabase
import com.example.storeapp.database.ItemViewModel
import com.example.storeapp.database.ItemViewModelFactory
import com.example.storeapp.databinding.FavsFragmentBinding
import com.example.storeapp.objects.Item
import com.example.storeapp.recyclerviews.FavsRecyclerAdapter
import com.example.storeapp.recyclerviews.MainRecyclerAdapter
import com.google.firebase.database.DatabaseReference

class FavsFragment: Fragment(R.layout.favs_fragment), ItemListener {

    private lateinit var binding: FavsFragmentBinding
    private lateinit var mainRecyclerView: RecyclerView
    private lateinit var viewModel: ItemViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        binding = FavsFragmentBinding.inflate(inflater, container, false)


        val favsDao = ItemDatabase.getInstance(requireContext()).item()
        val favsFactory = favsDao?.let { ItemViewModelFactory(it) }
        viewModel =
            ViewModelProvider(requireActivity(), favsFactory!!).get(ItemViewModel::class.java)
        val db = Room.databaseBuilder(requireContext(), ItemDatabase::class.java, "favs")
            .allowMainThreadQueries().build()

        val itemList = db.item()?.returnAllItems()


        mainRecyclerView = binding.favsRecycler
        mainRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        mainRecyclerView.setHasFixedSize(true)

        mainRecyclerView.setHasFixedSize(true);
        mainRecyclerView.setItemViewCacheSize(5);
        mainRecyclerView.setDrawingCacheEnabled(true);
        mainRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);



            mainRecyclerView.adapter = FavsRecyclerAdapter(itemList, this@FavsFragment)



        return binding.root
    }

    override fun itemClicked(item: Item?) {
        val bundle = bundleOf("item_id" to (item?.id),
            "item_price" to (item?.price), "item_name" to (item?.name),
            "item_category" to (item?.category), "item_imgUrl" to (item?.imgUrl))

        findNavController().navigate(R.id.action_favsFragment_to_detailsFragment,bundle )
    }



}