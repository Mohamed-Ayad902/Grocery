package com.example.grocery.repositories

import com.example.grocery.db.CartDao
import com.example.grocery.db.FavoriteDao
import com.example.grocery.models.*
import com.example.grocery.other.Constants
import com.example.grocery.other.Resource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class StoreRepositoryImpl @Inject constructor(
    private val fireStore: FirebaseFirestore,
    private val cartDao: CartDao,
    private val favoriteDao: FavoriteDao
) :

    StoreRepository {

    override suspend fun getOffers(): Resource<List<Offer>> {
        return try {
            val offersDocuments = fireStore.collection(Constants.OFFERS_COLLECTION).get().await()
            val offers = mutableListOf<Offer>()
            offersDocuments.forEach {
                offers.add(it.toObject(Offer::class.java))
            }
            Resource.Success(offers)
        } catch (e: Exception) {
            Resource.Error(e.message!!)
        }
    }

    override suspend fun getCategories(): Resource<List<Category>> {
        return try {
            val categoryDocuments =
                fireStore.collection(Constants.CATEGORY_COLLECTION).get().await()
            val categories = mutableListOf<Category>()
            categoryDocuments.forEach {
                categories.add(it.toObject(Category::class.java))
            }
            Resource.Success(categories)
        } catch (e: Exception) {
            Resource.Error(e.message!!)
        }
    }

    override suspend fun getLaptop(id: String): Resource<Laptop> {
        return try {
            val laptopDocument =
                fireStore.collection(Constants.PRODUCTS_COLLECTION).document(id).get().await()
            val laptop = laptopDocument.toObject(Laptop::class.java)
            if (laptop == null)
                Resource.Error("laptop document is null", null)
            Resource.Success(laptop!!)
        } catch (e: Exception) {
            Resource.Error(e.message!!)
        }
    }

    override suspend fun getProduct(id: String): Resource<Product> {
        return try {
            val productDocument =
                fireStore.collection(Constants.PRODUCTS_COLLECTION).document(id).get().await()
            val product = productDocument.toObject(Product::class.java)
            if (product == null)
                Resource.Error("product document is null", null)
            Resource.Success(product!!)
            Resource.Error("product document is null")
        } catch (e: Exception) {
            Resource.Error(e.message!!)
        }
    }

    // local database
    // favorite
    override suspend fun saveLaptop(laptop: Laptop) = favoriteDao.addLaptop(laptop)

    override suspend fun deleteSavedLaptop(laptop: Laptop) = favoriteDao.deleteLaptop(laptop)

    override fun getLaptopIfSaved(id: String) = favoriteDao.getLaptopIfSaved(id)

    override fun getLaptopFlowIfSaved(id: String) = favoriteDao.getLaptopFlow(id)

    override fun getAllSavedLaptops() = favoriteDao.getAllLaptops()

    override fun deleteAllSavedLaptops() = favoriteDao.deleteAllLaptops()


    // cart
    override suspend fun addItem(cart: Cart) = cartDao.addItem(cart)

    override suspend fun deleteItem(cart: Cart) = cartDao.deleteItem(cart)

    override suspend fun updateItem(cart: Cart) = cartDao.updateItem(cart)

    override fun getCartItems() = cartDao.getCartItems()

}