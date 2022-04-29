package com.josue.onlineshopapp.models


import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class User (
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val image: String = "",
    val mobile: Long = 0,
    val gender: String = "",
    val profileComplete: Int = 0
): Parcelable