package com.example.grocery.repositories

import android.util.Log
import com.example.grocery.models.*
import com.example.grocery.other.Constants
import com.example.grocery.other.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class StoreRepositoryImpl @Inject constructor(
    private val fireStore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) : StoreRepository {

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

    override suspend fun getHotAndNewItems(): Resource<List<HotAndNew>> {
        return try {
            val hotAndNewDocument =
                fireStore.collection(Constants.HOT_AND_NEW_COLLECTION).get().await()
            val hotAndNewItems = mutableListOf<HotAndNew>()
            hotAndNewDocument.forEach {
                hotAndNewItems.add(it.toObject(HotAndNew::class.java))
            }
            Resource.Success(hotAndNewItems)
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
        } catch (e: Exception) {
            Resource.Error(e.message!!)
        }
    }

    override suspend fun uploadOrder(
        orderLocation: String,
        totalPrice: Int,
        products: List<Cart>
    ): Resource<Order> {
        return try {
            val orderCollection = fireStore.collection(Constants.ORDERS_COLLECTION)
            val id = orderCollection.document().id
            val userId = firebaseAuth.currentUser?.uid!!

            val order =
                Order(
                    id,
                    userId,
                    System.currentTimeMillis(),
                    orderLocation,
                    Status.PLACED,
                    totalPrice,
                    products
                )
            orderCollection.document(id).set(order).await()
            Resource.Success(order)
        } catch (e: Exception) {
            Resource.Error(e.message!!)
        }
    }

}