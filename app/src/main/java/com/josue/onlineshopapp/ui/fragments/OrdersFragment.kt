package com.josue.onlineshopapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.josue.onlineshopapp.R
import com.josue.onlineshopapp.firestore.FirestoreClass
import com.josue.onlineshopapp.models.Order
import com.josue.onlineshopapp.ui.adapters.OrderAdapter

class OrdersFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_orders, container, false)
        return root
    }

    override fun onResume() {
        super.onResume()

        getMyOrdersList()
    }

    private fun getMyOrdersList() {
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getOrdersList(this@OrdersFragment)
    }

    fun populateOrdersList(ordersList: ArrayList<Order>) {
        hideProgressDialog()
        val recycleView = view?.findViewById<RecyclerView>(R.id.rv_my_order_items)
        val noOrders = view?.findViewById<TextView>(R.id.tv_no_orders_found)

        if (ordersList.size > 0) {

            recycleView?.visibility = View.VISIBLE
            noOrders?.visibility = View.GONE

            recycleView?.layoutManager = LinearLayoutManager(activity)
            recycleView?.setHasFixedSize(true)

            val ordersAdapter = OrderAdapter(requireActivity(), ordersList)
            recycleView?.adapter = ordersAdapter
        } else {
            recycleView?.visibility = View.GONE
            noOrders?.visibility = View.VISIBLE
        }
    }
}