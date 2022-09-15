package com.example.grocery.other

import android.view.View
import com.example.grocery.R
import com.example.grocery.ui.MainActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

fun MainActivity.showBottomNav() {
    val navigation = findViewById<BottomNavigationView>(R.id.bottomNavView)
    if (!navigation.isShown)
        navigation.show()
}

fun MainActivity.hideBottomNav() {
    val navigation = findViewById<BottomNavigationView>(R.id.bottomNavView)
    navigation.hide()
}

fun View.hide() {
    visibility = View.GONE
}

fun View.show() {
    visibility = View.VISIBLE
}