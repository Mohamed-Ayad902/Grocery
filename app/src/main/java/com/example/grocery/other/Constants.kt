package com.example.grocery.other

import android.graphics.Color

object Constants {

    // new api key for maps AIzaSyB8Xdpe-HJjRCPKcA6crbtbPflS-svXF4c old was AIzaSyCdZCnPJZ2oRXcNYeeVyj180w40IxgvbmM AIzaSyCdZCnPJZ2oRXcNYeeVyj180w40IxgvbmM

    const val PAY_MOB_BASE_URL = "https://accept.paymob.com/api/"
    const val PAY_MOB_API_KEY =
        "ZXlKaGJHY2lPaUpJVXpVeE1pSXNJblI1Y0NJNklrcFhWQ0o5LmV5SnVZVzFsSWpvaWFXNXBkR2xoYkNJc0luQnliMlpwYkdWZmNHc2lPalUzTnpRMU5pd2lZMnhoYzNNaU9pSk5aWEpqYUdGdWRDSjkubkticHhLamVNbmdrRHBNX2pheWI3NEFvcjh0RTlac09YckoyN0pWZ3JkZHM0NjFjaWhLZnlYYThLVS1hQ1I4RlJDcG91UGFYNWczVnk1N0hMODhsOUE="

    const val COLOR_GREEN: Int = Color.GREEN

    const val SHARED_PREFERENCE_NAME = "SHARED_PREFERENCE_NAME"
    const val KEY_IS_FIRST_TIME = "KEY_IS_FIRST_TIME"

    const val COUNT_DOWN_DELAY = 60000L
    const val COUNT_DOWN_INTERVAL = 1000L

    const val CATEGORY_LAPTOP = "laptop"

    const val USERS_COLLECTION = "users"
    const val PRODUCTS_COLLECTION = "laptops"
    const val OFFERS_COLLECTION = "offers"
    const val CATEGORY_COLLECTION = "category"
    const val HOT_AND_NEW_COLLECTION = "hotAndNew"
    const val ORDERS_COLLECTION = "orders"

    const val ENABLE_GPS_DIALOG = "ENABLE_GPS_DIALOG"
    const val PERMISSION_DIALOG = "PERMISSION_DIALOG"

    const val LOCATION_PERMISSION_REQ_CODE = 101
    const val ZOOM = 15f
}
