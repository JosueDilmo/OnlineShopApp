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

    lateinit var mViewModel: ProductViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val productRepository = ProductRepo(ProductDB(this))
        val productViewModelFactory = ProductViewModelFactory(productRepository)
        mViewModel = ViewModelProvider(this,productViewModelFactory).get(ProductViewModel::class.java)


        supportActionBar!!.setBackgroundDrawable(
            ContextCompat.getDrawable(this,
                R.drawable.app_gradient_color_background))

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment_activity_dashboard)
        //passing each menu ID as a set of Ids because each
        //menu should be considered as top level destinations
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_products,
                R.id.navigation_dashboard,
                R.id.navigation_orders
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    //fun onBackPressed to double back click to exit
    override fun onBackPressed() {
        doubleBackToExit()
    }


}