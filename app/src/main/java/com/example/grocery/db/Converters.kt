package com.example.grocery.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    fun saveStingList(list: List<String>): String? {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun getStingList(list: String): List<String> {
        return Gson().fromJson(
            list,
            object : TypeToken<List<String>>() {}.type
        )
    }
}