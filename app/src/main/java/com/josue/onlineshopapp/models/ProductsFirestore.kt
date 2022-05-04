package com.josue.onlineshopapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductsFirestore(
    val user_id: String = "",
    val user_name: String = "",
    val title: String = "",
    val price: Double = 0.0,
    val description: String = "",
    val image: String = "",
    var id: String = "",
): Parcelable
