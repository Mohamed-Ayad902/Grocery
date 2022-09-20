package com.example.grocery.other

import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.grocery.R
import com.example.grocery.ui.MainActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

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

fun Fragment.showToast(msg: String) {
    Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
}

fun Fragment.closeFragment() {
    findNavController().popBackStack()
}