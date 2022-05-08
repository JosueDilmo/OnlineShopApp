package com.josue.onlineshopapp.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.josue.onlineshopapp.R
import com.josue.onlineshopapp.firestore.FirestoreClass
import com.josue.onlineshopapp.models.CartItem
import com.josue.onlineshopapp.utils.Constants

class ProductDetailsActivity : BaseActivity() {

    private var mProductId: Int = -1
    private var mProductImage: String = ""
    private var mProductTitle: String = ""
    private var mProductCategory: String = ""
    private var mProductDescription: String = ""
    private var mProductPrice: Double = 0.0
    private var mProductUserId: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)

        setupActionBar()

        if (intent.hasExtra(Constants.PRODUCT_ID)){
            mProductId = intent.getIntExtra(Constants.PRODUCT_ID, -1)
            mProductTitle = intent.getStringExtra(Constants.PRODUCT_TITLE).toString()
            mProductUserId = intent.getStringExtra(Constants.EXTRA_PRODUCT_OWNER_ID).toString()
            mProductCategory = intent.getStringExtra(Constants.PRODUCT_CATEGORY).toString()
            mProductDescription = intent.getStringExtra(Constants.PRODUCT_DESCRIPTION).toString()
            mProductPrice = intent.getDoubleExtra(Constants.PRODUCT_PRICE, 0.0)
            mProductImage = intent.getStringExtra(Constants.PRODUCT_IMAGE).toString()

        }

        displayProductDetails()

    }

    private fun uploadProductDetails(){
        val cartItem = CartItem(
            FirestoreClass().getCurrentUserID(),
            mProductId,
            mProductTitle,
            mProductPrice,
            mProductDescription,
            mProductCategory,
            mProductImage,
            Constants.DEFAULT_CART_QUANTITY
        )
        FirestoreClass().uploadProductDetails(this, cartItem)

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


    fun productExistsInCart() {
        Toast.makeText(
            this@ProductDetailsActivity,
            resources.getString(R.string.product_already_in_cart),
            Toast.LENGTH_SHORT
        ).show()
        var btnGOTOCart = findViewById<Button>(R.id.btn_go_to_cart)
            btnGOTOCart.visibility = View.VISIBLE
                btnGOTOCart.setOnClickListener {
                    startActivity(Intent(this@ProductDetailsActivity, CartListActivity::class.java))
                    finish()
                }
        var btnADDTOCart = findViewById<Button>(R.id.btn_add_to_cart)
            btnADDTOCart.visibility = View.GONE
    }

    fun productNotInCart(){
        var btnGOTOCart = findViewById<Button>(R.id.btn_go_to_cart)
            btnGOTOCart.visibility = View.VISIBLE
                btnGOTOCart.setOnClickListener {
                    startActivity(Intent(this@ProductDetailsActivity, CartListActivity::class.java))
                    finish()
            }
        var btnADDTOCart = findViewById<Button>(R.id.btn_add_to_cart)
            btnADDTOCart.visibility = View.VISIBLE
                btnADDTOCart.setOnClickListener{
                    uploadProductDetails()
                    showProgressDialog(resources.getString(R.string.please_wait))
                }
    }

    private fun displayProductDetails(){
        val productImage = findViewById<ImageView>(R.id.iv_product_detail_image)
        Glide.with(this@ProductDetailsActivity).load(mProductImage).into(productImage)
        val productCategory = findViewById<TextView>(R.id.tv_product_details_category)
        val productTitle = findViewById<TextView>(R.id.tv_product_details_title)
        val productPrice = findViewById<TextView>(R.id.tv_product_details_price)
        val productDescription = findViewById<TextView>(R.id.tv_product_details_description)

        productPrice.text = "$${mProductPrice}"
        productTitle.text = mProductTitle
        productDescription.text = mProductDescription
        productCategory.text = mProductCategory


        FirestoreClass().checkIfItemExistInCart(this, mProductId)
    }


    private fun setupActionBar(){
        val toolbarProductDetailsAct = findViewById<Toolbar>(R.id.toolbar_product_details_activity)

        setSupportActionBar(toolbarProductDetailsAct)

        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_arrow_back_24)
        }

        toolbarProductDetailsAct.setNavigationOnClickListener {onBackPressed()}
    }


}