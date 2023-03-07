package com.example.storeapp.database

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class ItemViewModelFactory(private val dao: ItemDao): ViewModelProvider.Factory{
    override  fun <T: ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ItemViewModel::class.java)) {
            return ItemViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown View Model Class")
        }
}

