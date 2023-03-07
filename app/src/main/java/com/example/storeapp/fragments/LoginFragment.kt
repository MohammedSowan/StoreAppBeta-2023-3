package com.example.storeapp.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.storeapp.R
import com.example.storeapp.databinding.LoginFragmentBinding

class LoginFragment: Fragment(R.layout.login_fragment) {
    private lateinit var binding: LoginFragmentBinding

    val SHARED_PREFS = "sharedPrefs"
    val NAME = "name"
    val EMAIL = "email"
    val PHONE_NUMBER = "phone number"
    val STORE_NAME = "store name"
    val ADDRESS = "address"
    val LOGGED_IN = "logged in"



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        binding = LoginFragmentBinding.inflate(inflater, container, false)

        loadData()

        binding.button.setOnClickListener {
        saveData()

        }

        return binding.root
    }



    private fun saveData() {

        val nameTextView = binding.editText0
        val emailTextView = binding.editText1
        val phoneTextView = binding.editText2
        val addressTextView = binding.editText3

        if (nameTextView.text.isEmpty()||emailTextView.text.isEmpty()||phoneTextView.text.isEmpty()||addressTextView.text.isEmpty()){
            Toast.makeText(requireContext(), "please fill all the information", Toast.LENGTH_LONG).show()
        }

        else {
            val sharedPreferences =
                requireContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
            var editor = sharedPreferences.edit()
            editor.putString(NAME, nameTextView.text.toString())
            editor.putString(EMAIL, emailTextView.text.toString())
            editor.putString(PHONE_NUMBER, phoneTextView.text.toString())
            editor.putString(ADDRESS, addressTextView.text.toString())
            editor.putBoolean(LOGGED_IN, true);
            editor.apply()

            findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
        }
    }

    private fun loadData(){

        val sharedPreferences = requireContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        val nameText = sharedPreferences.getString(NAME, "");
        val emailText = sharedPreferences.getString(EMAIL, "");
        val phoneNumberText = sharedPreferences.getString(PHONE_NUMBER, "");
        val addressText = sharedPreferences.getString(ADDRESS, "");

        binding.editText0.setText(nameText);
        binding.editText1.setText(emailText);
        binding.editText2.setText(phoneNumberText);
        binding.editText3.setText(addressText);

        val isLoggedIn = sharedPreferences.getBoolean(LOGGED_IN,true);
    }


}



