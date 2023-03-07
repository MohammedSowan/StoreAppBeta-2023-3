package com.example.storeapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.storeapp.objects.Item

@Dao
interface ItemDao {


   // @Query("SELECT * FROM userinfo ORDER BY id DESC")
    //fun returnAllItems(): List<Item>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: Item)

    @Delete
    suspend fun deleteItem(item: Item)

    @Update
    suspend fun updateItem(item: Item)

    @Query("SELECT * FROM item_table")
    fun getAllItems(): LiveData<List<Item?>>

    @Query("SELECT * FROM item_table")
    fun returnAllItems(): List<Item?>

    @Query("DELETE FROM item_table")
    fun deleteAll()

}

