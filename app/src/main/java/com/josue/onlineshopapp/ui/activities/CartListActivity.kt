package com.josue.onlineshopapp.ui.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.josue.onlineshopapp.R
import com.josue.onlineshopapp.firestore.FirestoreClass
import com.josue.onlineshopapp.models.CartItem
import com.josue.onlineshopapp.ui.adapters.CartItemListAdapter
import kotlin.math.roundToInt

class CartListActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_list)

        setupActionBar()
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
        if (cartList.size > 0){
            recycleView.visibility = View.VISIBLE
            checkOutlL.visibility = View.VISIBLE
            noProduct.visibility = View.GONE

            recycleView.layoutManager = LinearLayoutManager(this@CartListActivity)
            recycleView.setHasFixedSize(true)
            val cartListAdapter = CartItemListAdapter(this@CartListActivity, cartList)
            recycleView.adapter = cartListAdapter

            var subTotal: Double = 0.0
            for (item in cartList){
                val price = item.price.toDouble()
                val quantity = item.cart_quantity.toInt()
                subTotal += (price * quantity)
            }
            findViewById<TextView>(R.id.tv_sub_total).text = "$$subTotal"
            var shippingCharge = (subTotal * 0.03).roundToInt()
            findViewById<TextView>(R.id.tv_shipping_charge).text = "$$shippingCharge"

            if (subTotal > 0){
                checkOutlL.visibility = View.VISIBLE

                val total = subTotal + shippingCharge
               findViewById<TextView>(R.id.tv_total_amount).text = "$$total"
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