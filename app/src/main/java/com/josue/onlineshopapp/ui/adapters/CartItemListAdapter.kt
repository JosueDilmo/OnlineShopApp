package com.josue.onlineshopapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.josue.onlineshopapp.R
import com.josue.onlineshopapp.models.CartItem

open class CartItemListAdapter(
    private val context: Context,
    private var list: ArrayList<CartItem>,
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.cart_view,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val cartItem = list[position]
        if (holder is MyViewHolder){
            Glide
                .with(context)
                .load(cartItem.image)
                .into(holder.itemView.findViewById(R.id.iv_cart_item_image))

            holder.itemView.findViewById<TextView>(R.id.tv_cart_item_title).text = cartItem.title
            holder.itemView.findViewById<TextView>(R.id.tv_cart_item_price).text = "$${cartItem.price}"
            holder.itemView.findViewById<TextView>(R.id.tv_cart_quantity).text = cartItem.cart_quantity




        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private class MyViewHolder(view: View): RecyclerView.ViewHolder(view)

}