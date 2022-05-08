package com.josue.onlineshopapp.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.SyncStateContract
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.josue.onlineshopapp.R
import com.josue.onlineshopapp.firestore.FirestoreClass
import com.josue.onlineshopapp.models.Order
import com.josue.onlineshopapp.ui.adapters.CartItemListAdapter
import com.josue.onlineshopapp.utils.Constants
import java.text.SimpleDateFormat
import java.util.*

class OrderDetailsActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_details)
        setupActionBar()

        var orderDetails: Order = Order()
        if (intent.hasExtra(Constants.EXTRA_ORDER_DETAILS)){
            orderDetails = intent.getParcelableExtra(Constants.EXTRA_ORDER_DETAILS)!!

        }
        setupOrdersDetails(orderDetails)


    }

    private fun setupOrdersDetails(orderDetails: Order){
        var recycleView = findViewById<RecyclerView>(R.id.rv_my_order_items_list)
        recycleView.layoutManager = LinearLayoutManager(this@OrderDetailsActivity)
        recycleView.setHasFixedSize(true)
        val cartListAdapter = CartItemListAdapter(this@OrderDetailsActivity, orderDetails.items)
        recycleView.adapter = cartListAdapter

        findViewById<TextView>(R.id.tv_order_details_id).text = orderDetails.title

        val dateFormat = "dd MMM yyyy HH:mm"
        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = orderDetails.order_dateTime
        val orderDateTime = formatter.format(calendar.time)
        findViewById<TextView>(R.id.tv_order_details_date).text = orderDateTime

        findViewById<TextView>(R.id.tv_my_order_details_address_type).text = orderDetails.address.type
        findViewById<TextView>(R.id.tv_my_order_details_full_name).text = orderDetails.address.name
        findViewById<TextView>(R.id.tv_my_order_details_address).text = "${orderDetails.address.address}, ${orderDetails.address.zipCode}"
        findViewById<TextView>(R.id.tv_my_order_details_additional_note).text = orderDetails.address.additionalNote

        if (orderDetails.address.otherDetails.isNotEmpty()) {
            findViewById<TextView>(R.id.tv_my_order_details_other_details).visibility = View.VISIBLE
            findViewById<TextView>(R.id.tv_my_order_details_other_details).text = orderDetails.address.otherDetails
        } else {
            findViewById<TextView>(R.id.tv_my_order_details_other_details).visibility = View.GONE
        }
        findViewById<TextView>(R.id.tv_my_order_details_mobile_number).text = orderDetails.address.mobileNumber
        findViewById<TextView>(R.id.tv_order_details_sub_total).text = orderDetails.sub_total_amount
        findViewById<TextView>(R.id.tv_order_details_shipping_charge).text = orderDetails.shipping_charge
        findViewById<TextView>(R.id.tv_order_details_total_amount).text = orderDetails.total_amount

    }


    private fun setupActionBar(){

        val toolbarOrderAct = findViewById<Toolbar>(R.id.toolbar_order_details_activity)

        setSupportActionBar(toolbarOrderAct)

        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_arrow_back_24)
        }

        toolbarOrderAct.setNavigationOnClickListener { onBackPressed() }
    }
}