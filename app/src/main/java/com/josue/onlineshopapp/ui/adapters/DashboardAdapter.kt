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
import com.josue.onlineshopapp.models.ProductsFirestore
import com.josue.onlineshopapp.ui.activities.ProductDetailsActivity
import com.josue.onlineshopapp.ui.fragments.DashboardFragment
import com.josue.onlineshopapp.utils.Constants

open class DashboardAdapter(private val context: Context,
                            private var list: ArrayList<ProductsFirestore>,
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
        val product = list[position]


        if (holder is MyViewHolder) {
            Glide
                .with(context)
                .load(product.image)
                .into(holder.itemView.findViewById(R.id.iv_product_detail_image))

            holder.itemView.findViewById<TextView>(R.id.tv_product_details_category).text = product.category
            holder.itemView.findViewById<TextView>(R.id.tv_product_details_title).text = product.title
            holder.itemView.findViewById<TextView>(R.id.tv_product_details_price).text = "$${product.price}"


            val btnDelete = holder.itemView.findViewById<ImageButton>(R.id.btn_delete)
                btnDelete.setOnClickListener {
                fragment.deleteProduct(product.product_id)
            }

            holder.itemView.setOnClickListener {
                val intent = Intent (context, ProductDetailsActivity::class.java)
                intent.putExtra(Constants.PRODUCT_ID, product.product_id)
                intent.putExtra(Constants.EXTRA_PRODUCT_ID, product.product_id)
                intent.putExtra(Constants.EXTRA_PRODUCT_OWNER_ID, product.user_id)
                intent.putExtra(Constants.PRODUCT_TITLE, product.title)
                intent.putExtra(Constants.PRODUCT_CATEGORY, product.category)
                intent.putExtra(Constants.PRODUCT_DESCRIPTION, product.description)
                intent.putExtra(Constants.PRODUCT_PRICE, product.price)
                intent.putExtra(Constants.PRODUCT_IMAGE, product.image)
                context.startActivity(intent)
            }

        }
    }


    override fun getItemCount(): Int {
        return list.size
    }


    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

}