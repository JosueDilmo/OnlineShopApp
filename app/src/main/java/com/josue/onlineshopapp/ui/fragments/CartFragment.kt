package com.josue.onlineshopapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.josue.onlineshopapp.R
import com.josue.onlineshopapp.ui.adapters.CartAdapter
import com.josue.onlineshopapp.fakestore.ProductViewModel
import com.josue.onlineshopapp.ui.activities.DashboardActivity
import com.josue.onlineshopapp.ui.activities.MainActivity
import java.math.BigDecimal


class CartFragment : Fragment(R.layout.fragment_cart) {

    lateinit var mViewModel: ProductViewModel
    lateinit var cartadapter: CartAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mViewModel = (activity as DashboardActivity).mViewModel

        setRecyclerview()

        mViewModel.getAllProducts().observe(viewLifecycleOwner, Observer {
            cartadapter.differ.submitList(it)
        })

        mViewModel.price.observe(viewLifecycleOwner, Observer {
            val tvTotalPrice = view.findViewById<TextView>(R.id.tvTotalPrice)
            tvTotalPrice.text = it.toString()
        })




        val btpay = view.findViewById<Button>(R.id.btpay)
        btpay.setOnClickListener {

            val amount = view.findViewById<TextView>(R.id.tvTotalPrice).text.toString().toBigDecimal()
            val check = BigDecimal.valueOf(0.0)

            if(amount == check){
                Toast.makeText(context,"Your cart is empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val amt = amount.toString().toDouble()
            val newamt = amt.times(100).toInt().toString()


        }

    }

    private fun setRecyclerview(){

        cartadapter = CartAdapter(mViewModel)

        view?.findViewById<RecyclerView>(R.id.rvCart)?.apply{
            adapter = cartadapter
            layoutManager= LinearLayoutManager(context)
        }
    }

}