package com.josue.onlineshopapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.josue.onlineshopapp.R
import com.josue.onlineshopapp.models.ProductsFirestore
import com.josue.onlineshopapp.ui.fragments.DashboardFragment
import org.w3c.dom.Text

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

        }
    }

    override fun getItemCount(): Int {
        return list.size
    }


    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}