package com.example.storeapp.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.storeapp.ItemListener
import com.example.storeapp.R
import com.example.storeapp.databinding.MainFragmentBinding
import com.example.storeapp.objects.Item
import com.example.storeapp.recyclerviews.MainRecyclerAdapter
import com.google.firebase.database.*


private lateinit var binding: MainFragmentBinding
private lateinit var mainRecyclerView: RecyclerView
private lateinit var itemArrayList: ArrayList<Item>
private lateinit var dbref: DatabaseReference
var isLoggedIn = false
 val LOGGED_IN = "logged in"
 val SHARED_PREFS = "sharedPrefs"


class MainFragment: Fragment(R.layout.main_fragment), ItemListener {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?



    ): View? {

        binding =MainFragmentBinding.inflate(inflater, container, false)
        loadData(isLoggedIn)

        if (isLoggedIn == false){
            findNavController().navigate(R.id.action_mainFragment_to_loginFragment)
            Toast.makeText(requireActivity(), "Logged is false", Toast.LENGTH_LONG).show()
        }

        binding.logOut.setOnClickListener {
            changeData()
            loadData(false)
            findNavController().navigate(R.id.action_mainFragment_to_loginFragment)

        }

        getItemData("ItemList0")

        binding.linearLayout0.setOnClickListener {
            getItemData("ItemList0")
        }

        binding.linearLayout1.setOnClickListener {
            getItemData("ItemList1")
        }

        binding.linearLayout2.setOnClickListener {
            getItemData("ItemList2")
        }

        binding.linearLayout3.setOnClickListener {
            getItemData("ItemList3")
        }
        binding.linearLayout4.setOnClickListener {
            getItemData("ItemList4")
        }


        return binding.root
    }

    private fun loadData(loggedIn: Boolean) {
        val sharedPreferences = requireContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        isLoggedIn = sharedPreferences.getBoolean(LOGGED_IN,loggedIn);
    }

    private fun changeData() {
        val sharedPreferences = requireContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        var editor = sharedPreferences.edit()

        editor.putBoolean(LOGGED_IN, false);

        editor.apply()
    }

    /*
        private fun downloadItemsData(categoryName: String) {
            CoroutineScope(Dispatchers.IO).launch {
                runBlocking {
                    getItemData(categoryName)
                } } }
    */
    private fun getItemData(categoryName: String) {

        mainRecyclerView = binding.mainRecyclerView
        mainRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        mainRecyclerView.setHasFixedSize(true)
        itemArrayList = arrayListOf<Item>()

    mainRecyclerView.setHasFixedSize(true);
    mainRecyclerView.setItemViewCacheSize(5);
    mainRecyclerView.setDrawingCacheEnabled(true);
    mainRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        itemArrayList.clear()

       val dbref = FirebaseDatabase.getInstance().getReference(categoryName)

        dbref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists())

                {
                    for (itemSnapshot in snapshot.children) {
                        var item = itemSnapshot.getValue(Item::class.java)
                        itemArrayList.add(item!!) } }

                mainRecyclerView.adapter = MainRecyclerAdapter(itemArrayList, this@MainFragment) }

            override fun onCancelled(error: DatabaseError) {
                print("error")
            } }) }

    override fun itemClicked(item: Item?) {

        val clickedItem = item?.let { Item(item.id, item?.name.toString(),
            item?.price, item?.imgUrl, 0, item?.category) }
        val bundle = bundleOf("item_id" to (item?.id),
            "item_price" to (item?.price), "item_name" to (item?.name),
            "item_category" to (item?.category), "item_imgUrl" to (item?.imgUrl))

        findNavController().navigate(R.id.action_mainFragment_to_detailsFragment,bundle )
    }
}