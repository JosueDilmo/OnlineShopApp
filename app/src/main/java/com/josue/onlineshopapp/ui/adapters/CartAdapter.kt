package com.josue.onlineshopapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.josue.onlineshopapp.R
import com.josue.onlineshopapp.fakestore.ProductViewModel
import com.josue.onlineshopapp.models.Products

class CartAdapter (val viewModel: ProductViewModel): RecyclerView.Adapter<CartAdapter.Cartviewholder>() {

        inner class Cartviewholder(item: View): RecyclerView.ViewHolder(item)

        private val differcallback=object: DiffUtil.ItemCallback<Products>(){
            override fun areItemsTheSame(oldItem: Products, newItem: Products): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Products, newItem: Products): Boolean {
                return oldItem == newItem
            }
        }

        val differ= AsyncListDiffer(this,differcallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Cartviewholder {
        return Cartviewholder(LayoutInflater.from(parent.context).inflate(R.layout.cart_view,parent,false))
    }

    override fun onBindViewHolder(holder: Cartviewholder, position: Int) {
        val product = differ.currentList[position]

        holder.itemView.apply {

            val ivArtImage = findViewById<ImageView>(R.id.ivArtImage)
            val tvPrice = findViewById<TextView>(R.id.tvPrice)
            val tvTitle = findViewById<TextView>(R.id.tvTitle)
            val tvDescription = findViewById<TextView>(R.id.tvDescription)
            val tvCurrentAmount = findViewById<TextView>(R.id.tvCurrentAmount)

            Glide.with(this).load(product.image).into(ivArtImage)
            tvPrice.text = product.price.toString()
            tvTitle.text = product.title
            tvDescription.text = product.description
            tvCurrentAmount.text = product.amt

            val btAdd = findViewById<ImageView>(R.id.btAdd)
            btAdd.setOnClickListener {
                var amount = tvCurrentAmount.text.toString().toInt()
                amount++
                product.amt = amount.toString()
                viewModel.upsert(product)
                tvCurrentAmount.text = amount.toString()
                viewModel.increase(product.price!!.toBigDecimal())
            }

            val btRemove = findViewById<ImageView>(R.id.btRemove)
            btRemove.setOnClickListener {
                var amount = tvCurrentAmount.text.toString().toInt()
                if(amount > 0){
                    amount--
                    product.amt = amount.toString()
                    viewModel.upsert(product)
                    tvCurrentAmount.text = amount.toString()
                    viewModel.decrease(product.price!!.toBigDecimal())
                }
                else{
                    Toast.makeText(context,"Amount already is zero", Toast.LENGTH_SHORT).show()
                }
            }

            val btDelete = findViewById<ImageView>(R.id.btDelete)
            btDelete.setOnClickListener {
                val amount = tvCurrentAmount.text.toString().toInt()
                viewModel.deletePrice(amount, product.price!!)
                viewModel.delete(product)
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}