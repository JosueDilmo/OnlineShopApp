package com.josue.onlineshopapp.ui.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.josue.onlineshopapp.R
import com.josue.onlineshopapp.fakestore.ProductViewModel
import com.josue.onlineshopapp.models.Products
import com.josue.onlineshopapp.ui.activities.ProductDetailsActivity
import com.josue.onlineshopapp.utils.Constants

class ProductAdapter (val viewModel: ProductViewModel
): RecyclerView.Adapter<ProductAdapter.Productviewholder>() {


    inner class Productviewholder(item: View): RecyclerView.ViewHolder(item)

    private val differcallback=object: DiffUtil.ItemCallback<Products>(){
        override fun areItemsTheSame(oldItem: Products, newItem: Products): Boolean {
            return oldItem.id==newItem.id
        }

        override fun areContentsTheSame(oldItem: Products, newItem: Products): Boolean {
            return oldItem==newItem
        }
    }

    val differ=AsyncListDiffer(this,differcallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Productviewholder {
        return Productviewholder(LayoutInflater.from(parent.context)
            .inflate(R.layout.product_view,parent,false))
    }

    override fun onBindViewHolder(holder: Productviewholder, position: Int) {

        val product = differ.currentList[position]

        holder.itemView.apply {

            val productImage = findViewById<ImageView>(R.id.iv_product_detail_image)
            val productTitle = findViewById<TextView>(R.id.tv_product_details_title)
            val productPrice = findViewById<TextView>(R.id.tv_product_details_price)
            val productCategory = findViewById<TextView>(R.id.tv_product_details_category)

            Glide.with(this).load(product.image).into(productImage)
            productPrice.text = "$${product.price}"
            productTitle.text = product.title
            productCategory.text = product.category


            holder.itemView.setOnClickListener{
                val intent = Intent(context, ProductDetailsActivity::class.java)
                intent.putExtra(Constants.EXTRA_PRODUCT_ID, product.id)
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
        return differ.currentList.size
    }


}


