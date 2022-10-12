package com.example.grocery.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.grocery.models.Laptop
import com.example.grocery.models.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    @Insert
    fun addLaptop(laptop: Laptop)

    @Insert
    fun addProduct(product: Product)

    @Delete
    fun deleteLaptop(laptop: Laptop)

    @Delete
    fun deleteProduct(product: Product)

    @Query("select * from laptop_table where id =:id")
    suspend fun getLaptopIfSaved(id: String): Laptop?

    @Query("select * from products_table where id =:id")
    fun getProductIfSaved(id: String): Product?

    @Query("select * from laptop_table where id =:id")
    fun getLaptopFlow(id: String): Flow<Laptop?>

    @Query("select * from products_table where id =:id")
    fun getProductFlow(id: String): Flow<Product?>

    @Query("select * from laptop_table")
    fun getAllLaptops(): Flow<List<Laptop>>

    @Query("select * from products_table")
    fun getAllProducts(): Flow<List<Product>>

    @Query("delete from laptop_table")
    fun deleteAllLaptops()

    @Query("delete from products_table")
    fun deleteAllProducts()


}