package com.example.grocery.repositories

import com.example.grocery.db.CartDao
import com.example.grocery.db.FavoriteDao
import com.example.grocery.models.Cart
import com.example.grocery.models.Laptop
import com.example.grocery.models.Product
import javax.inject.Inject

class LocalRepositoryImpl @Inject constructor(
    private val favoriteDao: FavoriteDao,
    private val cartDao: CartDao
) : LocalRepository {

    // favorite
    override suspend fun saveLaptop(laptop: Laptop) = favoriteDao.addLaptop(laptop)
    override suspend fun saveProduct(product: Product) = favoriteDao.addProduct(product)

    override suspend fun deleteSavedLaptop(laptop: Laptop) = favoriteDao.deleteLaptop(laptop)
    override suspend fun deleteSavedProduct(product: Product) = favoriteDao.deleteProduct(product)

    override suspend fun getLaptopIfSaved(id: String) = favoriteDao.getLaptopIfSaved(id)
    override suspend fun getProductIfSaved(id: String) = favoriteDao.getProductIfSaved(id)

    override fun getLaptopFlowIfSaved(id: String) = favoriteDao.getLaptopFlow(id)
    override fun getProductFlowIfSaved(id: String) = favoriteDao.getProductFlow(id)

    override fun getAllSavedLaptops() = favoriteDao.getAllLaptops()
    override fun getAllSavedProducts() = favoriteDao.getAllProducts()

    override fun deleteAllSavedLaptops() = favoriteDao.deleteAllLaptops()
    override fun deleteAllSavedProducts() = favoriteDao.deleteAllProducts()


    // cart
    override suspend fun addItem(cart: Cart) = cartDao.addItem(cart)

    override suspend fun deleteItem(cart: Cart) = cartDao.deleteItem(cart)

    override suspend fun updateItem(cart: Cart) = cartDao.updateItem(cart)

    override fun getCartItems() = cartDao.getCartItems()


}