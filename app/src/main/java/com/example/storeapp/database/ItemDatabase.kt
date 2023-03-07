package com.example.storeapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.storeapp.objects.Item

@Database(entities = [Item::class], version = 2)
abstract class ItemDatabase: RoomDatabase() {
    abstract fun item(): ItemDao?

    companion object {
        @Volatile
        private var INSTANCE: ItemDatabase? = null
        fun getInstance(context: Context): ItemDatabase{
            synchronized(this){
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ItemDatabase::class.java,
                        "item_table"
                    ).fallbackToDestructiveMigration().build()
                }
                return instance
            }
        }
    }
}