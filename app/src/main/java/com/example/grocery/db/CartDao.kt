package com.example.grocery.db

import androidx.room.*
import com.example.grocery.models.Cart

@Dao
interface CartDao {

    @Insert
    suspend fun addItem(cart: Cart)

    @Delete
    suspend fun deleteItem(cart: Cart)

    @Update
    suspend fun updateItem(cart: Cart)

    @Query("select * from cart_table")
    fun getCartItems(): List<Cart>

}