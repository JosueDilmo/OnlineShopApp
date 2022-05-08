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
import com.josue.onlineshopapp.models.CartItem
import com.josue.onlineshopapp.ui.adapters.CartItemListAdapter
import com.josue.onlineshopapp.utils.Constants
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.roundToInt
import kotlin.math.roundToLong

class CartListActivity : BaseActivity() {

    private lateinit var mCartListItem: ArrayList<CartItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_list)

        setupActionBar()

        findViewById<Button>(R.id.btn_checkout).setOnClickListener {
            val intent = Intent(this@CartListActivity, AddressListActivity::class.java)
            intent.putExtra(Constants.EXTRA_SELECT_ADDRESS, true)
            startActivity(intent)
        }


    }

    override fun onResume() {
        super.onResume()
        getCartItemList()
    }

    fun successCartItemList(cartList: ArrayList<CartItem>){
        hideProgressDialog()

            var recycleView = findViewById<RecyclerView>(R.id.rv_cart_items_list)
            var checkOutlL = findViewById<LinearLayout>(R.id.ll_checkout)
            var noProduct = findViewById<TextView>(R.id.tv_no_cart_item_found)

        mCartListItem = cartList

        if (mCartListItem.size > 0){
            recycleView.visibility = View.VISIBLE
            checkOutlL.visibility = View.VISIBLE
            noProduct.visibility = View.GONE

            recycleView.layoutManager = LinearLayoutManager(this@CartListActivity)
            recycleView.setHasFixedSize(true)
            val cartListAdapter = CartItemListAdapter(this@CartListActivity, mCartListItem/*, true*/)
            recycleView.adapter = cartListAdapter


            var subTotal: Double = 0.0
            for (item in mCartListItem){
                val price = item.price.roundToInt()
                val quantity = item.cart_quantity.toInt()
                subTotal += (price * quantity)

            }
            findViewById<TextView>(R.id.tv_sub_total).text = "$${subTotal}"
            var shippingCharge = (subTotal * 0.03).roundToInt()
            findViewById<TextView>(R.id.tv_shipping_charge).text = "$${shippingCharge}"

            if (subTotal > 0){
                checkOutlL.visibility = View.VISIBLE

                val total = (subTotal + shippingCharge).roundToInt()
               findViewById<TextView>(R.id.tv_total_amount).text = "$${total}"
            } else {
                checkOutlL.visibility = View.GONE
            }
        } else {
            recycleView.visibility = View.GONE
            checkOutlL.visibility = View.GONE
            noProduct.visibility = View.VISIBLE
        }

    }

    private fun getCartItemList(){
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getCartList(this@CartListActivity )
    }

    fun itemUpdateSuccess(){
        hideProgressDialog()
        getCartItemList()
    }

    fun itemRemovedSuccess(){
        hideProgressDialog()
        Toast.makeText(this,
            resources.getString(R.string.item_removed_successfully),
            Toast.LENGTH_SHORT
        ).show()

        getCartItemList()
    }

    private fun setupActionBar(){
        val toolbarCartListAct = findViewById<Toolbar>(R.id.toolbar_cart_list_activity)

        setSupportActionBar(toolbarCartListAct)

        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_arrow_back_24)
        }

        toolbarCartListAct.setNavigationOnClickListener {onBackPressed()}
    }
}