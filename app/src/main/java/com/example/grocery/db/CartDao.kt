package com.example.grocery.db

import androidx.room.*
import com.example.grocery.models.Cart
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {

    @Insert
    suspend fun addItem(cart: Cart)

    @Delete
    suspend fun deleteItem(cart: Cart)

    @Update
    suspend fun updateItem(cart: Cart)

    @Query("delete from cart_table")
    suspend fun deleteCartItems()

    @Query("select * from cart_table")
    fun getCartItems(): Flow<List<Cart>>

}