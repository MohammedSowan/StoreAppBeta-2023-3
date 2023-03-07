package com.example.storeapp.fragments

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.storeapp.R
import com.example.storeapp.database.ItemDatabase
import com.example.storeapp.database.ItemViewModel
import com.example.storeapp.database.ItemViewModelFactory
import com.example.storeapp.databinding.DetailsFragmentBinding
import com.example.storeapp.objects.Item
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class DetailsFragment: Fragment(R.layout.details_fragment) {

    private lateinit var binding: DetailsFragmentBinding
    private lateinit var viewModel: ItemViewModel
    private lateinit var favsViewModel: ItemViewModel
    private val itemsList = ArrayList<Item>()
    var isFav = false
    var ii =0

    var pDownX = 0
    var pDownY = 0
    var pUpX = 0
    var pUpY = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        binding = DetailsFragmentBinding.inflate(inflater, container, false)
        val dao = ItemDatabase.getInstance(requireContext()).item()
        val factory = dao?.let { ItemViewModelFactory(it) }
        viewModel =
            ViewModelProvider(requireActivity(), factory!!).get(ItemViewModel::class.java)

        val db = Room.databaseBuilder(requireContext(), ItemDatabase::class.java, "favs")
            .allowMainThreadQueries().build()


        val itemId = requireArguments().getInt("item_id")
        val itemName = requireArguments().getString("item_name")
        val imgUrl = requireArguments().getString("item_imgUrl")
        val price = requireArguments().getInt("item_price")
        val category = requireArguments().getString("item_category")

        val sharedPreferences =
            requireContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        var editor = sharedPreferences.edit()
        var id = sharedPreferences.getInt(itemName + "id", 0);

                val isFav = checkIfFav( itemName!!, isFav)
        if (isFav){
            binding.favIcon.setImageResource(R.drawable.fav_selected)
        }

        binding.itemTextViewTV.setText(itemName)

        val img = binding.itemImageView

        val manager: RequestManager? = img?.let { Glide.with(it) }
        manager?.load(imgUrl)?.thumbnail(0.05f)?.transition(
            DrawableTransitionOptions.withCrossFade()
        )?.into(img as ImageView)

        val amountText = binding.textViewTV
        var itemAmount = 4
        amountText.setText(itemAmount.toString())


        binding.buttonPlus.setOnClickListener {
            itemAmount++
            amountText.setText(itemAmount.toString())
        }

        binding.button5Plus.setOnClickListener {
            itemAmount += 5
            amountText.setText(itemAmount.toString())
        }

        binding.buttonMinus.setOnClickListener {
            if (itemAmount != 1 || itemAmount < 1) {
                itemAmount--
                amountText.setText(itemAmount.toString())
            }
        }

        binding.button5Minus.setOnClickListener {
            if (itemAmount < 6 || itemAmount == 6) {
                itemAmount = 1
                amountText.setText(itemAmount.toString())
            } else {
                itemAmount -= 5
                amountText.setText(itemAmount.toString())
            }
        }

        binding.favIcon.setOnClickListener {

            val sharedPreferences =
                requireContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
            val isFav = sharedPreferences.getBoolean(itemName,isFav);

            if(!isFav){
                binding.favIcon.setImageResource(R.drawable.fav_selected)
                    addToFavs(db, itemName!!,price, imgUrl!!, category!!)
                Toast.makeText(
                    requireContext(), "fav item was added" +
                            "", Toast.LENGTH_LONG
                ).show()
            }

            else {
                binding.favIcon.setImageResource(R.drawable.fav_unselected)
                removeFromFavs(db,itemName!!)
            }

        }

        binding.orderButton.setOnClickListener {
            var iii = false

            viewModel.Items.observe(viewLifecycleOwner){

                var iii = false

                for (item in it) {
                    if (item?.name.equals(itemName)) {
                        viewModel.updateItem(
                            Item(
                                item!!.id,
                                item!!.name,
                                item!!.price,
                                item.imgUrl,
                                itemAmount + item.amount!!,
                                category
                            )
                        )
                        ii++
                    } else{
                        ii++
                        iii = ii != it.size
                    }
                }


            if (!iii) {
                var newid = id++
                editor.putInt("id", newid)
                viewModel.insertItem(
                    Item(newid, itemName.toString(), price, imgUrl, itemAmount, category)
                )

                Toast.makeText(
                    requireContext(), "ur item was added" +
                            "", Toast.LENGTH_LONG
                ).show()
                //   }
                //  }

            findNavController().popBackStack()}
            else findNavController().popBackStack()
        }}

        binding.detailsFragmentView.setOnTouchListener(object :
            OnSwipeTouchListener(requireContext()) {

            override fun onSwipeLeft() {

                Toast.makeText(requireContext(), "left", Toast.LENGTH_LONG).show()
                navigationLeft(category.toString(), +1)

            }

            override fun onSwipeRight() {
                Toast.makeText(requireContext(), "right", Toast.LENGTH_LONG).show()
                navigationLeft(category.toString(), -1)

            }
        }


        )

        return binding.root

    }

    private fun removeFromFavs(db: ItemDatabase,itemName:String) {
        val sharedPreferences =
            requireContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        var editor = sharedPreferences.edit()
        editor.putBoolean(itemName, false);
        editor.apply()
        val itemList = db.item()?.returnAllItems()
        val itemId = sharedPreferences.getInt(itemName + "id",0);


        CoroutineScope(Dispatchers.IO).launch {
            runBlocking {
                db.item()?.deleteItem(itemList?.get(itemId)!!)
            }}

    }

    private fun checkIfFav( itemName:String, isFav:Boolean ): Boolean {
        val sharedPreferences = requireContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        val isFav = sharedPreferences.getBoolean(itemName,isFav);
        return isFav

    }


    private fun addToFavs(db: ItemDatabase, itemName: String, price:Int, imgUrl: String, category:String) {

        val itemList = db.item()?.returnAllItems()
        val itemId = itemList!!.size

        CoroutineScope(Dispatchers.IO).launch {
                runBlocking {

                    db.item()
                        ?.insertItem(
                            Item(
                                itemId,
                                itemName,
                                price,
                                imgUrl,
                                0,
                                category
                            )
                        )
                }

                val sharedPreferences =
                    requireContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
                var editor = sharedPreferences.edit()
                editor.putInt(itemName, itemId);
                editor.putBoolean(itemName, true);
            editor.apply()
            }}



    private fun moveItem(NextItem: Item) {
        val bundle = bundleOf(
            "item_id" to (NextItem?.id),
            "item_price" to (NextItem?.price),
            "item_name" to (NextItem?.name),
            "item_category" to (NextItem?.category),
            "item_imgUrl" to (NextItem?.imgUrl)
        )
        findNavController().popBackStack()
        findNavController().navigate(R.id.action_mainFragment_to_detailsFragment, bundle)
    }

    private fun navigationLeft(categoryName: String, direction: Int) {
        val id = requireArguments().getInt("item_id")

        val dbref = FirebaseDatabase.getInstance().getReference(categoryName)

        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (itemSnapshot in snapshot.children) {
                        var item = itemSnapshot.getValue(Item::class.java)
                        itemsList.add(item!!)
                    }

                    if (itemsList.get(id).id == itemsList.size - 1 && direction == +1) {
                        val NextItem = itemsList.get(0)
                        moveItem(NextItem)
                    } else if (itemsList.get(id).id == 0 && direction == -1) {
                        val NextItem = itemsList.get(itemsList.size - 1)
                        moveItem(NextItem)
                    } else {
                        val NextItem = itemsList.get(id + direction)
                        moveItem(NextItem)

                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })


    }

    open class OnSwipeTouchListener(ctx: Context) : View.OnTouchListener {

        private val gestureDetector: GestureDetector

        companion object {

            private val SWIPE_THRESHOLD = 100
            private val SWIPE_VELOCITY_THRESHOLD = 100
        }

        init {
            gestureDetector = GestureDetector(ctx, GestureListener())
        }

        override fun onTouch(v: View, event: MotionEvent): Boolean {
            return gestureDetector.onTouchEvent(event)
        }

        private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {


            override fun onDown(e: MotionEvent): Boolean {
                return true
            }

            override fun onFling(
                e1: MotionEvent,
                e2: MotionEvent,
                velocityX: Float,
                velocityY: Float
            ): Boolean {
                var result = false
                try {
                    val diffY = e2.y - e1.y
                    val diffX = e2.x - e1.x
                    if (Math.abs(diffX) > Math.abs(diffY)) {
                        if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                            if (diffX > 0) {
                                onSwipeRight()
                            } else {
                                onSwipeLeft()
                            }
                            result = true
                        }
                    } else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY > 0) {
                            onSwipeBottom()
                        } else {
                            onSwipeTop()
                        }
                        result = true
                    }
                } catch (exception: Exception) {
                    exception.printStackTrace()
                }

                return result
            }


        }

        open fun onSwipeRight() {}

        open fun onSwipeLeft() {}

        open fun onSwipeTop() {}

        open fun onSwipeBottom() {}
    }
}


