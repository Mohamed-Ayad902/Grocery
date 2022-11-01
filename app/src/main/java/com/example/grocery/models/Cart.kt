package com.example.grocery.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Entity(tableName = "cart_table")
@Parcelize
data class Cart(
    @PrimaryKey(autoGenerate = true)
    val itemId: Int = 0,
    val productName: String = "",
    val productImage: String = "",
    val productId: String = "",
    val productBrand: String = "",
    val productPrice: Int = 0,
    var productQuantity: Int = 0,
) : Serializable, Parcelable