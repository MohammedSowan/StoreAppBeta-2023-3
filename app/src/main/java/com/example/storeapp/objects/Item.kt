package com.example.storeapp.objects

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.temporal.TemporalAmount

@Entity(tableName = "item_table")
data class Item(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "item_id")
    val id:Int = 0,
    @ColumnInfo(name = "item_name") var name: String?  = "",
    @ColumnInfo(name = "item_price") var price: Int? = 0,
    @ColumnInfo(name = "item_img") var imgUrl: String? = "",
    @ColumnInfo(name = "item_amount") var amount: Int? = 0,
    @ColumnInfo(name = "item_category") var category: String? = "0"){
    override fun toString(): String = name + amount
}
