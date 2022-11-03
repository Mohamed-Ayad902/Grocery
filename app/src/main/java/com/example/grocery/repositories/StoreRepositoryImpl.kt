package com.example.grocery.repositories

import android.util.Log
import com.example.grocery.models.*
import com.example.grocery.other.Constants
import com.example.grocery.other.Resource
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
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

    override suspend fun getAllProductsByCategory(category: String): Resource<List<Product>> {
        return try {
            val productsDocument =
                fireStore.collection(Constants.PRODUCTS_COLLECTION)
                    .whereEqualTo("category", category).get().await()
            val products = mutableListOf<Product>()
            for (document in productsDocument.documents) {
                document.toObject(Product::class.java)?.let { products.add(it) }
            }
            Resource.Success(products)
        } catch (e: Exception) {
            Resource.Error(e.message!!)
        }
    }

    override suspend fun getAllLaptops(): Resource<List<Laptop>> {
        return try {
            val laptopsDocument =
                fireStore.collection(Constants.PRODUCTS_COLLECTION)
                    .whereEqualTo("category", Constants.CATEGORY_LAPTOP).get().await()
            val laptops = mutableListOf<Laptop>()
            for (document in laptopsDocument.documents) {
                document.toObject(Laptop::class.java)?.let { laptops.add(it) }
            }
            Resource.Success(laptops)
        } catch (e: Exception) {
            Resource.Error(e.message!!)
        }
    }

    override suspend fun getLaptopById(id: String): Resource<Laptop> {
        Log.d("mohamed", "getLaptopById: $id")
        return try {
            val laptopDocument =
                fireStore.collection(Constants.PRODUCTS_COLLECTION).document(id).get().await()
            val laptop = laptopDocument.toObject(Laptop::class.java)
            if (laptop == null)
                Resource.Error("laptop document is null", null)
            Resource.Success(laptop!!)
        } catch (e: Exception) {
            Resource.Error(e.stackTraceToString())
        }
    }

    override suspend fun getProductById(id: String): Resource<Product> {
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
        orderLocation: LatLng,
        totalPrice: Int,
        products: List<Cart>,
        paymentMethod: PaymentMethod
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
                    orderLocation.latitude,
                    orderLocation.longitude,
                    Status.PLACED,
                    totalPrice,
                    products,
                    paymentMethod
                )
            orderCollection.document(id).set(order).await()
            Resource.Success(order)
        } catch (e: Exception) {
            Resource.Error(e.message!!)
        }
    }

    override suspend fun getOrderById(id: String): Resource<Order> {
        try {
            val request =
                fireStore.collection(Constants.ORDERS_COLLECTION).document(id).get().await()

            val order = request.toObject(Order::class.java)
            order?.let {
                return Resource.Success(it)
            } ?: return Resource.Error("order found null")
        } catch (e: Exception) {
            return Resource.Error(e.message!!)
        }
    }

    override suspend fun getUserOrders(): Resource<List<Order>> {
        return try {
            val request =
                fireStore.collection(Constants.ORDERS_COLLECTION)
                    .whereEqualTo("userId", firebaseAuth.currentUser?.uid!!)
                    .orderBy("time", Query.Direction.DESCENDING).get()
                    .await()
            val orders = mutableListOf<Order>()
            for (document in request.documents) {
                document.toObject(Order::class.java)?.let { orders.add(it) }
            }
            Resource.Success(orders)
        } catch (e: Exception) {
            Resource.Error(e.message!!)
        }
    }

    override suspend fun search(query: String): Resource<LaptopsProducts> {
        return try {
            val request =
                fireStore.collection(Constants.PRODUCTS_COLLECTION)
                    .whereArrayContains("searchKeywords", query).get().await()

            val laptops = mutableListOf<Laptop>()
            val products = mutableListOf<Product>()
            val laptopsProducts = LaptopsProducts(laptops, products)
            for (document in request.documents) {
                val category = document.getString("category")
                if (category == "laptop")
                    document.toObject(Laptop::class.java)?.let { laptops.add(it) }
                else
                    document.toObject(Product::class.java)?.let { products.add(it) }
            }
            Resource.Success(laptopsProducts)
        } catch (e: Exception) {
            Resource.Error(e.message!!)
        }
    }

    /*
//    suspend fun updateDocuments() {
//        val documents =
//            fireStore.collection(Constants.PRODUCTS_COLLECTION)
//                .whereNotEqualTo("category", "laptop")
//                .get().await()
//        for (document in documents.documents) {
//            val laptop = document.toObject(Product::class.java)
//            laptop?.let {
//                it.searchKeywords = generateSearchKeywords(it.name)
//
//                fireStore.collection(Constants.PRODUCTS_COLLECTION).document(it.id)
//                    .set(it).await()
//                Log.e("mohamed", "updateDocuments: ${it.id} ->>>>  ${it.searchKeywords}")
//            }
//        }
//    }
//
//    private fun generateSearchKeywords(inputText: String): List<String> {
//        var inputString = inputText.lowercase()
//        val keywords = mutableListOf<String>()
//
//        // split all words from the string
//        val words = inputString.split(" ")
//
//        for (word in words) {
//            var appendString = ""
//
//            // for every char in the hole string
//            for (charPosition in inputString.indices) {
//                appendString += inputString[charPosition].toString()
//                keywords.add(appendString)
//            }
//
//            // remove first word from the string
//            inputString = inputString.replace("$word ", "")
//        }
//        return keywords
//    }
    */

}