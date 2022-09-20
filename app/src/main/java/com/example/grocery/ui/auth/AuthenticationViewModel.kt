package com.example.grocery.ui.auth

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grocery.other.AuthResource
import com.example.grocery.other.Resource
import com.example.grocery.repositories.AuthenticationRepository
import com.google.firebase.auth.PhoneAuthCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor
    (private val repository: AuthenticationRepository) : ViewModel() {

    private val _auth = MutableStateFlow<AuthResource>(AuthResource.Idle)
    val auth: StateFlow<AuthResource> = _auth

    private val _signInStatus = MutableStateFlow<Resource<Unit?>>(Resource.Idle())
    val signInStatus: StateFlow<Resource<Unit?>> = _signInStatus

    private val _userInfo = MutableStateFlow<Resource<String>>((Resource.Idle()))
    val userInfo: StateFlow<Resource<String>> = _userInfo


    fun checkIfFirstAppOpened() = repository.checkIfFirstAppOpened()

    fun checkIfUserLoggedIn() = repository.checkIfUserLoggedIn()

    fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        _signInStatus.value = Resource.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            _signInStatus.value = (repository.signInWithCredential(credential))
        }
    }

    fun phoneAuthCallBack() = repository.phoneAuthCallBack(_auth)

    fun uploadUserInformation(userName: String, imageUri: Uri?, userLocation: String) {
        _userInfo.value = Resource.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            _userInfo.value = (repository.uploadUserInformation(userName, imageUri, userLocation))
        }

    }

}