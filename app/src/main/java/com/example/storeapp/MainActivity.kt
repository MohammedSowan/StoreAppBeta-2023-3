package com.example.storeapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.arch.core.executor.ArchTaskExecutor
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.storeapp.fragments.CartFragment
import com.example.storeapp.fragments.FavsFragment
import com.example.storeapp.fragments.MainFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val cart: View
        val main: View
        val database = Firebase.database
        val myRef = database.getReference("ItemList4")

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        //  val navController = findNavController(R.id.fragmentContainerView)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController


        NavigationUI.setupWithNavController(bottomNavigationView, navController)



        bottomNavigationView.setOnNavigationItemReselectedListener {

            try {


            when(it.itemId){
                R.id.favsFragment -> {
                    navController.navigateUp()
                    navController.navigateUp()
                    navController.navigate(R.id.action_mainFragment_to_favsFragment)
                }
                R.id.mainFragment -> {
                    navController.navigateUp()
                    navController.navigateUp()
                    navController.navigate(R.id.action_mainFragment_self)
                }


                R.id.cartFragment -> {
                    navController.navigateUp()
                    navController.navigateUp()
                    navController.navigate(R.id.action_mainFragment_to_cartFragment)
                }
            }
            } catch (e: java.lang.IllegalArgumentException) {
                navController.popBackStack()
            }

        }



/*
        var List = arrayListOf<Item>()

        List.add(Item(0, "בובות צו'מפי פותח פה 50 סמ", 40, "https://drive.google.com/uc?id=19s-n6m4Q-mesNRlIFAbIhk3BTKyHvTpo",0, "ItemList4"))


        myRef.setValue(List)*/

    }

    private fun replaceFragment(fragment: Fragment){
        if (fragment !=null){
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.nav_host_fragment, fragment)
            transaction.commit()
        }
    }

    fun nav(fragment: Fragment){
        val intent = Intent(this, fragment::class.java)
        startActivity(intent)
    }

}