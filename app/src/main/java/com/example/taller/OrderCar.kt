package com.example.taller

// Modelo Order.kt
data class Order(
    val id: Int,
    val clientName: String,
    val clientID: String,
    val phone: String,
    val email: String,
    val address: String,
    val total: String,
    val createdAt: String,
    val updatedAt: String,
    val cars: List<OrderCar>
)

// Modelo OrderCar.kt
data class OrderCar(
    val id: Int,
    val name: String,
    val model: String,
    val brand: String,
    val price: String,
    val description: String,
    val stock: Int,
    val image: String? // Puede ser null
)
