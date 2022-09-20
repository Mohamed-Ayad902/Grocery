package com.example.grocery.models

import java.io.Serializable

data class User(
    var id: String = "",
    var name: String = "",
    val image: String,
    var location: String = "",
):Serializable