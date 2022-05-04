package com.josue.onlineshopapp.ui.activities

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.josue.onlineshopapp.R
import com.josue.onlineshopapp.databinding.ActivityDashboardBinding
import com.josue.onlineshopapp.fakestore.ProductDB
import com.josue.onlineshopapp.fakestore.ProductRepo
import com.josue.onlineshopapp.fakestore.ProductViewModel
import com.josue.onlineshopapp.fakestore.ProductViewModelFactory

class DashboardActivity : BaseActivity() {

    lateinit var mViewModel: ProductViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val productRepository = ProductRepo(ProductDB(this))
        val productViewModelFactory = ProductViewModelFactory(productRepository)
        mViewModel = ViewModelProvider(this,productViewModelFactory).get(ProductViewModel::class.java)



        supportActionBar!!.setBackgroundDrawable(
            ContextCompat.getDrawable(this@DashboardActivity,
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