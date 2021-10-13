package com.jfsb.myvie.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.PopupMenu
import com.jfsb.myvie.R
import com.jfsb.myvie.databinding.ActivityMainBinding
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        navController = findNavController(R.id.main_fragment)
        setupActionBarWithNavController(navController)
        setupSmoothBottomMenu()
    }

    private fun setupSmoothBottomMenu() {
        val popupMenu = PopupMenu(this, null)
        popupMenu.inflate(R.menu.menu_bottom)
        val menu = popupMenu.menu
        binding.bottomBar.setupWithNavController(menu, navController)
    }
}