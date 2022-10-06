package com.example.grocery.ui

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.grocery.R
import com.example.grocery.databinding.ActivityMainBinding
import com.example.grocery.other.hideBottomNav
import com.example.grocery.other.showBottomNav
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController =
            (supportFragmentManager.findFragmentById(binding.fragmentContainerView.id) as NavHostFragment).navController
        binding.bottomNavView.setupWithNavController((navController))

        binding.bottomNavView.setupWithNavController(navController)

        navController
            .addOnDestinationChangedListener { _, destination, _ ->
                when (destination.id) {
                    R.id.homeFragment, R.id.cartFragment, R.id.favoriteFragment,
                    R.id.exploreFragment, R.id.settingsFragment, R.id.checkOutOrderFragment -> showBottomNav()
                    else -> hideBottomNav()
                }
            }

    }

}