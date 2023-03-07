package com.example.storeapp.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.storeapp.ItemListener
import com.example.storeapp.R
import com.example.storeapp.SwipeToDeleteCallback
import com.example.storeapp.database.ItemDatabase
import com.example.storeapp.database.ItemViewModel
import com.example.storeapp.database.ItemViewModelFactory
import com.example.storeapp.databinding.CartFragmentBinding
import com.example.storeapp.objects.Item
import com.example.storeapp.recyclerviews.CartRecyclerViewAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class CartFragment: Fragment(R.layout.cart_fragment), ItemListener {

    private lateinit var viewModel: ItemViewModel
    private lateinit var CartItemsRecyclerView: RecyclerView
    private lateinit var adapter: CartRecyclerViewAdapter

    val SHARED_PREFS = "sharedPrefs"
    val NAME = "name"
    val EMAIL = "email"
    val PHONE_NUMBER = "phone number"
    val STORE_NAME = "store name"
    val ADDRESS = "address"

    private lateinit var nameText: String
    private lateinit var emailText: String
    private lateinit var addressText: String
    private lateinit var phoneNumberText: String

    var totalPrice = 0
    var totalAmount = 0


    lateinit var binding: CartFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?


    ): View? {
        binding = CartFragmentBinding.inflate(inflater, container, false)
        CartItemsRecyclerView = binding.CartItemsRecyclerView

        val dao = ItemDatabase.getInstance(requireContext()).item()
        val factory = dao?.let { ItemViewModelFactory(it) }

        viewModel =
            ViewModelProvider(requireActivity(), factory!!).get(ItemViewModel::class.java)



        viewModel.Items.observe(requireActivity()) {

          val list = it
            for (item in list) {
            totalAmount = totalAmount + item?.amount!!
            totalPrice = totalPrice + item?.price!!
        }

            initRecyclerView(list)

        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(CartItemsRecyclerView)

        binding.orderButton.setOnClickListener {

            showDialog(list)
        }

        }
        return binding.root
    }

    private fun sendEmail(it: List<Item?>) {

            loadData()
            val sharedPreferences = requireContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
            val nameText = sharedPreferences.getString(NAME, "");
            val emailText = sharedPreferences.getString(EMAIL, "");
            val phoneNumberText = sharedPreferences.getString(PHONE_NUMBER, "");
            val addressText = sharedPreferences.getString(ADDRESS, "");
        val email = Intent(Intent.ACTION_SEND)
            email.putExtra(Intent.EXTRA_EMAIL, arrayOf("muhamdsowan0924@gmail.com"))
            email.putExtra(Intent.EXTRA_SUBJECT, "new order")

            email.putExtra(
                Intent.EXTRA_TEXT, """הזמנה חדשה מאת:$nameText
כתובת:$addressText
מספר טלפון:$phoneNumberText
אימייל:$emailText
 
${it.toString()}

 סך הכל הוא: ${totalPrice.toString()}"""
            )
            email.type = "message/rfc822"
            startActivity(Intent.createChooser(email, "Send Mail Using :"))

        runBlocking {
            val job = launch(Dispatchers.Default) {
                println("${Thread.currentThread()} has run.")
                viewModel.deleteAll()
            }}

            findNavController().popBackStack()
        }


    private fun initRecyclerView(it: List<Item?>) {
        CartItemsRecyclerView.layoutManager = LinearLayoutManager(view?.getContext())
            adapter = CartRecyclerViewAdapter(it as ArrayList<Item>, this@CartFragment, viewModel)
            CartItemsRecyclerView.adapter = adapter
            adapter.notifyDataSetChanged()
    }

    val swipeToDeleteCallback = object : SwipeToDeleteCallback() {
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition
            runBlocking {
                adapter.deleteItem(position) } }

    }

    fun loadData() {
        val sharedPreferences = requireContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        val nameText = sharedPreferences.getString(NAME, "");
        val emailText = sharedPreferences.getString(EMAIL, "");
        val phoneNumberText = sharedPreferences.getString(PHONE_NUMBER, "");
        val addressText = sharedPreferences.getString(ADDRESS, "");

    }

     override fun itemClicked(item: Item?) {

     }


     private fun showDialog(it: List<Item?>) {

         val builder = AlertDialog.Builder(requireContext())
         builder.setTitle("confirm your order")
         builder.setMessage("after pressing yes your order will be sent, you brought $totalAmount item, and your total is $totalPrice\"")
         builder.setIcon(android.R.drawable.ic_dialog_alert)

         builder.setPositiveButton("Yes"){dialogInterface, which ->
             sendEmail(it)
         }
         builder.setNeutralButton("Cancel"){dialogInterface , which ->

         }
         builder.setNegativeButton("No"){dialogInterface, which ->
         }
         val alertDialog: AlertDialog = builder.create()
         alertDialog.setCancelable(false)
         alertDialog.show()
     }
 }



