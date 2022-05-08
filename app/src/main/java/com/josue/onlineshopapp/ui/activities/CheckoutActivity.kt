package com.josue.onlineshopapp.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.josue.onlineshopapp.R
import com.josue.onlineshopapp.firestore.FirestoreClass
import com.josue.onlineshopapp.models.Address
import com.josue.onlineshopapp.models.CartItem
import com.josue.onlineshopapp.models.Order
import com.josue.onlineshopapp.ui.adapters.CartItemListAdapter
import com.josue.onlineshopapp.utils.Constants
import kotlin.math.roundToInt
import kotlin.math.roundToLong

class CheckoutActivity : BaseActivity() {

    private var mAddressDetails: Address? = null
    private lateinit var mCartListItem: ArrayList<CartItem>
    private var mSubTotal: Double = 0.0
    private var mTotalAmount: Double = 0.0
    private var mShippingCharge: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        setupActionBar()

        if(intent.hasExtra(Constants.EXTRA_SELECTED_ADDRESS)){
            mAddressDetails = intent.getParcelableExtra(Constants.EXTRA_SELECTED_ADDRESS)
        }

        if (mAddressDetails != null){
            findViewById<TextView>(R.id.tv_checkout_address_type).text = mAddressDetails?.type
            findViewById<TextView>(R.id.tv_checkout_full_name).text = mAddressDetails?.name
            findViewById<TextView>(R.id.tv_checkout_address).text = "${mAddressDetails!!.address}, ${mAddressDetails!!.zipCode}"
            findViewById<TextView>(R.id.tv_checkout_additional_note).text = mAddressDetails?.additionalNote
            findViewById<TextView>(R.id.tv_mobile_number).text = mAddressDetails?.mobileNumber

            if (mAddressDetails?.otherDetails!!.isNotEmpty()){
                findViewById<TextView>(R.id.tv_checkout_other_details).text = mAddressDetails?.otherDetails
            }


        }

        getAllCartList()

        val btnPlaceOrder = findViewById<Button>(R.id.btn_place_order)
        btnPlaceOrder.setOnClickListener() {
            placeAnOrder()
        }
    }


    fun successCartListFromFirestore(cartList: ArrayList<CartItem>){
        hideProgressDialog()
        mCartListItem = cartList

        var recycleView = findViewById<RecyclerView>(R.id.rv_cart_list_checkout_items)
        recycleView.layoutManager = LinearLayoutManager(this@CheckoutActivity)
        recycleView.setHasFixedSize(true)
        val cartListAdapter = CartItemListAdapter(this@CheckoutActivity, mCartListItem)
        recycleView.adapter = cartListAdapter

        for (item in mCartListItem) {
            val quantity = item.cart_quantity.toInt()
            val price = item.price.toDouble()
            mSubTotal += (price * quantity).roundToInt()
        }
        mShippingCharge = (mSubTotal * 0.03).roundToInt().toDouble()
        findViewById<TextView>(R.id.tv_checkout_shipping_charge).text = "$${mShippingCharge}"
        findViewById<TextView>(R.id.tv_checkout_sub_total).text = "$${mSubTotal}"

        val llPlaceOrder = findViewById<LinearLayout>(R.id.ll_checkout_place_order)
        if (mSubTotal > 0) {
            llPlaceOrder.visibility = View.VISIBLE

            mTotalAmount = (mSubTotal + mShippingCharge).roundToInt().toDouble()

            findViewById<TextView>(R.id.tv_checkout_total_amount).text = "$${mTotalAmount}"
        } else {
            llPlaceOrder.visibility = View.GONE
        }



    }

    private fun getAllCartList(){
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getCartList(this@CheckoutActivity)
    }


    private fun placeAnOrder() {
        showProgressDialog(resources.getString(R.string.please_wait))

        if (mAddressDetails != null){
            val order = Order(
                FirestoreClass().getCurrentUserID(),
                mCartListItem,
                mAddressDetails!!,
            "My order ${System.currentTimeMillis()}",
                mCartListItem[0].image,
                mSubTotal.toString(),
                mShippingCharge.toString(),
                mTotalAmount.toString(),
                System.currentTimeMillis()
            )

        FirestoreClass().placeOrder(this@CheckoutActivity, order)
        }

    }

    fun updateAllSuccess(){
        hideProgressDialog()

        Toast.makeText(this@CheckoutActivity,
            resources.getString(R.string.msg_your_order_added_successfully), Toast.LENGTH_SHORT)
            .show()

        val intent = Intent(this@CheckoutActivity, DashboardActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()

    }


    fun orderPlacedSuccess() {
        FirestoreClass().updateAll(this@CheckoutActivity,mCartListItem )
    }


    private fun setupActionBar(){

        val toolbarCheckoutAct = findViewById<Toolbar>(R.id.toolbar_checkout_activity)

        setSupportActionBar(toolbarCheckoutAct)

        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_arrow_back_24)
        }

        toolbarCheckoutAct.setNavigationOnClickListener { onBackPressed() }
    }
}