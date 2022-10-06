package com.example.grocery.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "laptop_table")
data class Laptop(
    @PrimaryKey var id: String = "",
    val name: String = "",
    val brand: String = "",
    val processor: String = "",
    val image: String = "",
    val ram: Int = 0,
    val ssd: Int = 0,
    val hdd: Int = 0,
    val category: String = "",
    val price: Int = 0,
    val gpu: String = "",
    val cpuSpeed: Double = 0.0,
    val description: String = "",
    val camera: Int = 0,
    val size: Double = 0.0,
): Serializable