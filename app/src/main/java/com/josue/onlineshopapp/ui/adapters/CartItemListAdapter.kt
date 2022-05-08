package com.josue.onlineshopapp.ui.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.josue.onlineshopapp.R
import com.josue.onlineshopapp.firestore.FirestoreClass
import com.josue.onlineshopapp.models.CartItem
import com.josue.onlineshopapp.ui.activities.CartListActivity
import com.josue.onlineshopapp.ui.activities.CheckoutActivity
import com.josue.onlineshopapp.ui.activities.OrderDetailsActivity
import com.josue.onlineshopapp.ui.activities.ProductDetailsActivity
import com.josue.onlineshopapp.utils.Constants

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
        if (holder is MyViewHolder) {
            Glide
                .with(context)
                .load(cartItem.image)
                .into(holder.itemView.findViewById(R.id.iv_cart_item_image))

            holder.itemView.findViewById<TextView>(R.id.tv_cart_item_title).text = cartItem.title
            holder.itemView.findViewById<TextView>(R.id.tv_cart_item_price).text ="$${cartItem.price}"
            holder.itemView.findViewById<TextView>(R.id.tv_cart_quantity).text = cartItem.cart_quantity



            when (context) {
                is CartListActivity -> {

                    holder.itemView.findViewById<ImageButton>(R.id.delete_cart_item)
                        .setOnClickListener {
                            context.showProgressDialog(context.resources.getString(R.string.please_wait))
                            FirestoreClass().deleteProductFromCart(context, cartItem.product_id)
                        }

                    holder.itemView.findViewById<ImageButton>(R.id.decrease_cart_item)
                        .setOnClickListener {
                            if (cartItem.cart_quantity == "1") {
                                FirestoreClass().deleteProductFromCart(
                                    context,
                                    cartItem.product_id
                                )
                            } else {
                                val cartQuantity: Int = cartItem.cart_quantity.toInt()
                                val itemHashMap = HashMap<String, Any>()

                                itemHashMap[Constants.CART_QUANTITY] =
                                    (cartQuantity - 1).toString()

                                if (context is CartListActivity) {
                                    context.showProgressDialog(context.resources.getString(R.string.please_wait))
                                }

                                FirestoreClass().updateMyCart(
                                    context,
                                    cartItem.product_id,
                                    itemHashMap
                                )
                            }

                        }
                    holder.itemView.findViewById<ImageButton>(R.id.increase_cart_item)
                        .setOnClickListener {
                            val cartQuantity: Int = cartItem.cart_quantity.toInt()
                            val itemHashMap = HashMap<String, Any>()
                            if (cartQuantity >= 1) {
                                itemHashMap[Constants.CART_QUANTITY] =
                                    (cartQuantity + 1).toString()

                                if (context is CartListActivity) {
                                    context.showProgressDialog(context.resources.getString(R.string.please_wait))
                                    FirestoreClass().updateMyCart(
                                        context,
                                        cartItem.product_id,
                                        itemHashMap
                                    )
                                }

                            }
                        }
                }

                is CheckoutActivity -> {
                    holder.itemView.findViewById<ImageButton>(R.id.delete_cart_item).visibility = View.GONE
                    holder.itemView.findViewById<ImageButton>(R.id.decrease_cart_item).visibility = View.GONE
                    holder.itemView.findViewById<ImageButton>(R.id.increase_cart_item).visibility = View.GONE


                }

                is OrderDetailsActivity -> {
                    holder.itemView.findViewById<ImageButton>(R.id.delete_cart_item).visibility = View.GONE
                    holder.itemView.findViewById<ImageButton>(R.id.decrease_cart_item).visibility = View.GONE
                    holder.itemView.findViewById<ImageButton>(R.id.increase_cart_item).visibility = View.GONE

                    holder.itemView.setOnClickListener {
                        val intent = Intent(context, ProductDetailsActivity::class.java)
                        intent.putExtra(Constants.PRODUCT_ID, cartItem.product_id)
                        intent.putExtra(Constants.PRODUCT_TITLE, cartItem.title)
                        intent.putExtra(Constants.PRODUCT_CATEGORY, cartItem.category)
                        intent.putExtra(Constants.PRODUCT_DESCRIPTION, cartItem.description)
                        intent.putExtra(Constants.PRODUCT_PRICE, cartItem.price)
                        intent.putExtra(Constants.PRODUCT_IMAGE, cartItem.image)
                        context.startActivity(intent)
                    }

                    }
                }


            }



        }




    override fun getItemCount(): Int {
        return list.size
    }

    private class MyViewHolder(view: View): RecyclerView.ViewHolder(view)

}