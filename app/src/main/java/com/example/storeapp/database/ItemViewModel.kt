package com.example.storeapp.database

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storeapp.objects.Item
import kotlinx.coroutines.launch

class ItemViewModel(private  val dao: ItemDao): ViewModel() {

    val Items = dao.getAllItems()

    fun insertItem(item: Item)=viewModelScope.launch {
        dao.insertItem(item)
    }

    fun updateItem(item: Item)=viewModelScope.launch {
        dao.updateItem(item)
    }

    fun deleteItem(item: Item?)=viewModelScope.launch {
        if (item != null) {
            dao.deleteItem(item)
        }
    }

    fun deleteAll() {
        dao.deleteAll()
    }
}

