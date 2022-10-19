package com.example.grocery.repositories

import android.net.Uri
import com.example.grocery.models.User
import com.example.grocery.other.AuthResource
import com.example.grocery.other.Constants.USERS_COLLECTION
import com.example.grocery.other.Resource
import com.example.grocery.other.SharedPreferenceHelper
import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class AuthenticationRepository @Inject constructor(
    private val storage: FirebaseStorage,
    private val sp: SharedPreferenceHelper,
    private val fireStore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {

    private val userUid by lazy { auth.uid!! }
    private val userCollection by lazy { fireStore.collection(USERS_COLLECTION) }

    fun checkIfFirstAppOpened(): Boolean = sp.checkIfFirstAppOpened()

    fun checkIfUserLoggedIn() = auth.currentUser != null

    fun phoneAuthCallBack(_auth: MutableStateFlow<AuthResource>): PhoneAuthProvider.OnVerificationStateChangedCallbacks {
        return object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                _auth.value =
                    AuthResource.SuccessWithCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                _auth.value = AuthResource.Error(e)
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                _auth.value =
                    AuthResource.SuccessWithCode(verificationId, token)
            }
        }
    }

    suspend fun uploadUserInformation(
        userName: String,
        imageUri: Uri?,
        userLocation: String
    ): Resource<String> {
        return try {
            var accountStatusMessage = "Account created successfully"
            if (imageUri != null) {
                val imagePath = uploadUserImage(imageUri)
                val user = User(userUid, userName, imagePath, userLocation)
                userCollection.document(userUid).set(user).await()
            } else {
                val user = User(userUid, userName, "", userLocation)
                userCollection.document(userUid).set(user).await()
                accountStatusMessage = "Account updated successfully"
            }
            Resource.Success(accountStatusMessage)
        } catch (e: Exception) {
            Resource.Error(e.message!!)
        }
    }

    private suspend fun uploadUserImage(imageUri: Uri): String {
        val uploadingResult =
            storage.reference.child("${USERS_COLLECTION}/${System.currentTimeMillis()}.jpg")
                .putFile(imageUri).await()
        return uploadingResult.metadata?.reference?.downloadUrl?.await().toString()
    }

    suspend fun signInWithCredential(credential: AuthCredential): Resource<Unit?> {
        return try {
            auth.signInWithCredential(credential).await()
            Resource.Success(null)
        } catch (e: Exception) {
            Resource.Error(e.message!!)
        }
    }

    // check if user has data into firebase fireStore or not.
    fun getUserInformation(userInfo: MutableStateFlow<Resource<User>>) {
        fireStore.collection(USERS_COLLECTION).document(userUid)
            .addSnapshotListener { value, error ->
                if (value == null) {
                    userInfo.value = (Resource.Error(error?.message!!))
                } else {
                    val user = value.toObject(User::class.java)
                    if (user != null)
                        userInfo.value = (Resource.Success(user))
                    else
                        userInfo.value = (Resource.Error("User value was null"))

                }
            }
    }

    suspend fun changeUserLocation(location: String): Boolean {
        return try {
            fireStore.collection(USERS_COLLECTION).document(userUid)
                .update(mapOf("location" to location)).await()
            true
        } catch (e: Exception) {
            false
        }
    }

}