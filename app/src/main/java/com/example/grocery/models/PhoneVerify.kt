package com.example.grocery.models

import com.google.firebase.auth.PhoneAuthProvider
import java.io.Serializable


data class PhoneVerify(
    val verificationId: String,
    val verificationToken: PhoneAuthProvider.ForceResendingToken,
    val phoneNumber: String
) : Serializable