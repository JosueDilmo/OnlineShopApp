package com.josue.onlineshopapp.ui.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.josue.onlineshopapp.R
import com.josue.onlineshopapp.models.Address
import com.josue.onlineshopapp.ui.activities.AddEditAddressActivity
import com.josue.onlineshopapp.ui.activities.CheckoutActivity
import com.josue.onlineshopapp.utils.Constants

open class AddressListAdapter (
    val context: Context,
    private var list: ArrayList<Address>,
    private var selectAddress: Boolean
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_address_layout,
                parent,
                false
            )
        )
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val address = list[position]

        if (holder is MyViewHolder) {
            holder.itemView.findViewById<TextView>(R.id.tv_address_full_name).text = address.name
            holder.itemView.findViewById<TextView>(R.id.tv_address_type).text = address.type
            holder.itemView.findViewById<TextView>(R.id.tv_address_details).text = "${address.address}, ${address.zipCode}"
            holder.itemView.findViewById<TextView>(R.id.tv_address_mobile_number).text = address.mobileNumber

            if (selectAddress){
                holder.itemView.setOnClickListener{
                    val intent = Intent(context, CheckoutActivity::class.java)
                    intent.putExtra(Constants.EXTRA_SELECTED_ADDRESS, address)
                    context.startActivity(intent)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    fun notifyEditItem(activity: Activity, position: Int){
        val intent = Intent(context, AddEditAddressActivity::class.java)
        intent.putExtra(Constants.EXTRA_ADDRESS_DETAILS, list[position])
        activity.startActivityForResult(intent, Constants.ADD_ADDRESS_REQUEST_CODE)
        notifyItemChanged(position)
    }
}
