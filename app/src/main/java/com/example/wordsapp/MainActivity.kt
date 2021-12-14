package com.example.wordsapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.wordsapp.databinding.ActivityMainBinding
import androidx.navigation.ui.AppBarConfiguration
import com.example.wordsapp.ui.SearchActivity


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val drawerLayout = binding.drawerLayout

        binding.imageMenu.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        val navigationView = binding.navigationView
        navigationView.itemIconTintList = null
       val navController = Navigation.findNavController(this, R.id.navHostFragment)
        NavigationUI.setupWithNavController(navigationView, navController)

        val title = binding.title

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            title.text = destination.label
        }
        binding.search.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }

    }


}