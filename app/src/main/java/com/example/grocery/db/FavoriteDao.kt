package com.example.grocery.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.grocery.models.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Insert
    fun addProduct(product: Product)

    @Delete
    fun deleteProduct(product: Product)

    @Query("select * from favorite_table where id =:id")
    fun getProduct(id: String)

    @Query("select * from favorite_table")
    fun getAllProducts(): Flow<List<Product>>

    @Query("delete from favorite_table")
    fun deleteAllProducts()

}