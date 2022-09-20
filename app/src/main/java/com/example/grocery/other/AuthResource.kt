package com.example.grocery.other

import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

sealed class AuthResource {
    object Idle: AuthResource()
    object Loading: AuthResource()
    data class SuccessWithCredential(val data: PhoneAuthCredential): AuthResource()
    data class SuccessWithCode(val verificationId: String, val verificationToken: PhoneAuthProvider.ForceResendingToken): AuthResource()
    data class Error(val error: FirebaseException): AuthResource()
}