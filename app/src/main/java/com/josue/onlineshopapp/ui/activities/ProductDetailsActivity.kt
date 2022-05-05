package com.josue.onlineshopapp.ui.activities

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
import com.josue.onlineshopapp.models.CartItem
import com.josue.onlineshopapp.utils.Constants

class ProductDetailsActivity : BaseActivity(), View.OnClickListener {

    private var mProductId: Int = -1
    private var mProductImage: String = ""
    private var mProductTitle: String = ""
    private var mProductCategory: String = ""
    private var mProductDescription: String = ""
    private var mProductPrice: Double = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)

        setupActionBar()

        if (intent.hasExtra(Constants.EXTRA_PRODUCT_ID)){
            mProductId = intent.getIntExtra(Constants.EXTRA_PRODUCT_ID,-1)
            mProductTitle = intent.getStringExtra(Constants.PRODUCT_TITLE).toString()
            mProductCategory = intent.getStringExtra(Constants.PRODUCT_CATEGORY).toString()
            mProductDescription = intent.getStringExtra(Constants.PRODUCT_DESCRIPTION).toString()
            mProductPrice = intent.getDoubleExtra(Constants.PRODUCT_PRICE, 0.0)
            mProductImage = intent.getStringExtra(Constants.PRODUCT_IMAGE).toString()

        }

       /* if(FirestoreClass().getCurrentUserID() == CartItem().user_id){
        }else{*/
            FirestoreClass().checkIfItemExistInCart(this, mProductId.toString())
        //}

        displayProductDetails()

    }

    override fun onClick(v: View?) {
        if(v!=null) {
            when (v.id) {
                R.id.btn_add_to_cart -> {
                    uploadProductDetails()
                }
            }
        }
    }

    private fun uploadProductDetails(){
        val cartItem = CartItem(
            FirestoreClass().getCurrentUserID(),
            mProductId.toString(),
            mProductTitle,
            mProductPrice,
            mProductDescription,
            mProductCategory,
            mProductImage,
            Constants.DEFAULT_CART_QUANTITY
        )


        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().uploadProductDetails(this, cartItem)

    }

    fun productUploadSuccess(){
        hideProgressDialog()
        Toast.makeText(
            this@ProductDetailsActivity,
            resources.getString(R.string.product_added_to_cart_success),
            Toast.LENGTH_SHORT
        ).show()
        //finish()
        val btnAddToCart = findViewById<Button>(R.id.btn_add_to_cart)
        btnAddToCart.visibility = View.GONE
        val btnGoToCart = findViewById<Button>(R.id.btn_go_to_cart)
        btnGoToCart.visibility = View.VISIBLE
    }


    fun productExistsInCart() {
        hideProgressDialog()
        var btnGoToCart = findViewById<Button>(R.id.btn_go_to_cart)
        btnGoToCart.visibility = View.VISIBLE
        var btnAddToCart = findViewById<Button>(R.id.btn_add_to_cart)
        btnAddToCart.visibility = View.VISIBLE
    }


    private fun displayProductDetails(){

        showProgressDialog(resources.getString(R.string.please_wait))

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


        var btnAddToCart = findViewById<Button>(R.id.btn_add_to_cart)
        btnAddToCart.setOnClickListener(this)
        var btnGoToCart = findViewById<Button>(R.id.btn_go_to_cart)
        btnGoToCart.setOnClickListener(this)

    }


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

}