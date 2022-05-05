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
import com.josue.onlineshopapp.models.CartItem
import com.josue.onlineshopapp.ui.activities.ProductDetailsActivity
import com.josue.onlineshopapp.ui.fragments.DashboardFragment
import com.josue.onlineshopapp.utils.Constants

open class DashboardAdapter(private val context: Context,
                            private var list: ArrayList<CartItem>,
                            private val fragment: DashboardFragment
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.product_view_dashboard,
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val cartItem = list[position]


        if (holder is MyViewHolder) {
            Glide
                .with(context)
                .load(cartItem.image)
                .into(holder.itemView.findViewById(R.id.iv_product_detail_image))

            holder.itemView.findViewById<TextView>(R.id.tv_product_details_category).text = cartItem.category
            holder.itemView.findViewById<TextView>(R.id.tv_product_details_title).text = cartItem.title
            holder.itemView.findViewById<TextView>(R.id.tv_product_details_price).text = "$${cartItem.price}"


            val btnDelete = holder.itemView.findViewById<ImageButton>(R.id.btn_delete)
                btnDelete.setOnClickListener {
                fragment.deleteProduct(cartItem.product_id)
            }

            holder.itemView.setOnClickListener {
                val intent = Intent (context, ProductDetailsActivity::class.java)
                intent.putExtra(Constants.EXTRA_PRODUCT_ID, cartItem.product_id)
                intent.putExtra(Constants.EXTRA_PRODUCT_OWNER_ID, cartItem.user_id)
                intent.putExtra(Constants.PRODUCT_TITLE, cartItem.title)
                intent.putExtra(Constants.PRODUCT_CATEGORY, cartItem.category)
                intent.putExtra(Constants.PRODUCT_DESCRIPTION, cartItem.description)
                intent.putExtra(Constants.PRODUCT_PRICE, cartItem.price)
                intent.putExtra(Constants.PRODUCT_IMAGE, cartItem.image)
                context.startActivity(intent)
            }

        }
    }


    override fun getItemCount(): Int {
        return list.size
    }


    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

}