package com.josue.onlineshopapp.ui.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.josue.onlineshopapp.R
import com.josue.onlineshopapp.models.Order
import com.josue.onlineshopapp.ui.activities.OrderDetailsActivity
import com.josue.onlineshopapp.utils.Constants


open class OrderAdapter(
    private val context: Context,
    private var list: ArrayList<Order>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_list_layout,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val order = list[position]

        if (holder is MyViewHolder) {

            Glide
                .with(context)
                .load(order.image)
                .into(holder.itemView.findViewById(R.id.iv_item_image))

            holder.itemView.findViewById<TextView>(R.id.tv_item_name).text = order.title
            holder.itemView.findViewById<TextView>(R.id.tv_item_price).text = "$${order.total_amount}"

           holder.itemView.setOnClickListener {
                val intent = Intent(context, OrderDetailsActivity::class.java)
                intent.putExtra(Constants.EXTRA_ORDER_DETAILS, order)
                context.startActivity(intent)
            }

        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}