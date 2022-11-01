package com.example.grocery.models

import com.google.android.gms.maps.model.LatLng
import java.io.Serializable

data class User(
    var id: String = "",
    var name: String = "",
    val image: String = "",
    val longT: LatLng = LatLng(25.0, 29.0)
//    var location: String = "",
) : Serializable