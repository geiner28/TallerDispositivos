package com.example.taller

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CartItem(
    val product: Product,
    var quantity: Int

) : Parcelable
