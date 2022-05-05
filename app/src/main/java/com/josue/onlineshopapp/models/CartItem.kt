package com.josue.onlineshopapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CartItem (
    val user_id: String = "",
    var product_id: String = "",
    val title: String = "",
    val price: Double = 0.0,
    val description: String = "",
    val category: String = "",
    val image: String = "",
    var cart_quantity: String = "",
    var id: String = "",
): Parcelable