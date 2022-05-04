package com.josue.onlineshopapp.ui.activities

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.josue.onlineshopapp.R
import com.josue.onlineshopapp.firestore.FirestoreClass
import com.josue.onlineshopapp.models.ProductsFirestore
import com.josue.onlineshopapp.utils.Constants

class ProductDetailsActivity : BaseActivity(), View.OnClickListener {

    private var mProductId: String = ""
    private var mProductImage: String = ""
    private var mProductTitle: String = ""
    private var mProductCategory: String = ""
    private var mProductDescription: String = ""
    private var mProductPrice: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)

        //action bar function
        setupActionBar()

        if (intent.hasExtra(Constants.PRODUCT_ID)){
            mProductId = intent.getIntArrayExtra(Constants.PRODUCT_ID).toString()
            mProductImage = intent.getStringExtra(Constants.PRODUCT_IMAGE).toString()
            mProductTitle = intent.getStringExtra(Constants.PRODUCT_TITLE).toString()
            mProductDescription = intent.getStringExtra(Constants.PRODUCT_DESCRIPTION).toString()
            mProductCategory = intent.getStringExtra(Constants.PRODUCT_CATEGORY).toString()
            mProductPrice = intent.getDoubleExtra(Constants.PRODUCT_PRICE, 0.0)

        }

        displayProductDetails()

        val btnAddToCart = findViewById<Button>(R.id.btn_add_to_cart)
        btnAddToCart.setOnClickListener(this)

    }

    fun productUploadSuccess(){
        hideProgressDialog()
        Toast.makeText(
            this@ProductDetailsActivity,
            resources.getString(R.string.product_added_to_cart_success),
            Toast.LENGTH_SHORT
        ).show()
        finish()
    }

    private fun uploadProductDetails(){
        val username = this.getSharedPreferences(
           Constants.ONSHOPPES_PREFERENCES, Context.MODE_PRIVATE
        ).getString(Constants.LOGGED_IN_USERNAME, "")!!

        val product = ProductsFirestore(
            FirestoreClass().getCurrentUserID(),
            username,
            mProductTitle,
            mProductCategory,
            mProductPrice,
            mProductDescription,
            mProductImage,
            mProductId,
        )
        FirestoreClass().uploadProductDetails(this, product)

    }


    private fun displayProductDetails(){

        val ivArticleImage = findViewById<ImageView>(R.id.iv_product_detail_image)
        Glide.with(this@ProductDetailsActivity)
            .load(mProductImage)
            .into(ivArticleImage)
        val categorytv = findViewById<TextView>(R.id.tv_product_details_category)
        val titletv = findViewById<TextView>(R.id.tv_product_details_title)
        val pricetv = findViewById<TextView>(R.id.tv_product_details_price)
        val descriptiontv = findViewById<TextView>(R.id.tv_product_details_description)

        pricetv.text = "$${mProductPrice}"
        titletv.text = mProductTitle
        descriptiontv.text = mProductDescription
        categorytv.text = mProductCategory
    }


    //action bar function
    private fun setupActionBar(){
        //setting up the toolbar with back button
        val toolbarProductDetailsAct = findViewById<Toolbar>(R.id.toolbar_product_details_activity)

        setSupportActionBar(toolbarProductDetailsAct)

        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_arrow_back_24)
        }

        toolbarProductDetailsAct.setNavigationOnClickListener {onBackPressed()}
    }


    override fun onClick(v: View?) {
        if(v!=null) {
            when (v.id) {
                R.id.btn_add_to_cart -> {
                    uploadProductDetails()
                    showProgressDialog(resources.getString(R.string.please_wait))
                }
            }
        }

    }

}