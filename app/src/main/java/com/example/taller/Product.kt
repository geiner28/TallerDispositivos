package com.example.taller

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val id: Int,
    val name: String,
    val model: String,
    val brand: String,
    val price: Double,
    val description: String,
    val stock: Int,
    val image: String
) : Parcelable
