package com.josue.onlineshopapp.ui.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.josue.onlineshopapp.R
import com.josue.onlineshopapp.fakestore.ProductDB
import com.josue.onlineshopapp.fakestore.ProductRepo
import com.josue.onlineshopapp.fakestore.ProductViewModel
import com.josue.onlineshopapp.fakestore.ProductViewModelFactory
import com.josue.onlineshopapp.utils.Constants

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }
}