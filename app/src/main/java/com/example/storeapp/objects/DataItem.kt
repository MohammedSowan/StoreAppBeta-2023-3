package com.example.storeapp.objects

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.storeapp.R
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAmount

@Entity(tableName = "task_item_table")
class DataItem(
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "price") var price: Int,
    @ColumnInfo(name = "amount") var amount: Int,
    @ColumnInfo(name = "imgUrl") var imgUrl: String?,
    @ColumnInfo(name = "category") var category: Int,

    @PrimaryKey(autoGenerate = true) var id: Int = 0
)
