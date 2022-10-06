package com.example.grocery.other

import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
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

fun ImageView.loadImage(link: String) {
    Glide.with(context).load(link).placeholder(R.drawable.ic_logo).into(this)
}

fun Fragment.showToast(msg: String) {
    Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
}

fun Fragment.closeFragment() {
    findNavController().popBackStack()
}