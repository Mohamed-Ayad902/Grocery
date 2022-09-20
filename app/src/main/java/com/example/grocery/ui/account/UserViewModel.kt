package com.example.grocery.ui.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grocery.models.User
import com.example.grocery.other.Resource
import com.example.grocery.repositories.AuthenticationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val repository: AuthenticationRepository) :
    ViewModel() {
    private val _user = MutableStateFlow<Resource<User>>(Resource.Idle())
    val user: StateFlow<Resource<User>> = _user

    private val _userLocation = MutableStateFlow<String?>(null)
    val userLocation: StateFlow<String?> = _userLocation

    init {
        getUserInformation()
    }

    fun setUserLocation(location: String) {
        _userLocation.value = location
    }

    private fun getUserInformation() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getUserInformation(_user)
        }
    }

    fun changUserLocation(location: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.changeUserLocation(location!!)
            if (result)
                getUserInformation()
        }
    }
}