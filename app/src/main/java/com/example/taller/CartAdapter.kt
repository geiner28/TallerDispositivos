package com.example.taller

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.bumptech.glide.Glide

class CartAdapter(
    private val cartItems: MutableList<CartItem>,
    private val onCartUpdated: () -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivProductImage: ImageView = itemView.findViewById(R.id.ivProductImage)
        val tvProductName: TextView = itemView.findViewById(R.id.tvProductName)
        val tvProductPrice: TextView = itemView.findViewById(R.id.tvProductPrice)
        val tvProductQuantity: TextView = itemView.findViewById(R.id.tvProductQuantity)
        val btnIncrease: Button = itemView.findViewById(R.id.btnIncrease)
        val btnDecrease: Button = itemView.findViewById(R.id.btnDecrease)
        val btnDelete: Button = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cartItem = cartItems[position]

        // Cargar la imagen del producto con Glide
        Glide.with(holder.itemView.context)
            .load(cartItem.product.image) // URL de la imagen del producto

            .into(holder.ivProductImage)

        holder.tvProductName.text = cartItem.product.name
        holder.tvProductPrice.text = "Precio: $${cartItem.product.price}"
        holder.tvProductQuantity.text = "${cartItem.quantity}"

        // Botón incrementar
        holder.btnIncrease.setOnClickListener {
            cartItem.quantity++
            notifyItemChanged(position)
            onCartUpdated()
        }

        // Botón decrementar
        holder.btnDecrease.setOnClickListener {
            if (cartItem.quantity > 1) {
                cartItem.quantity--
                notifyItemChanged(position)
                onCartUpdated()
            }
        }

        // Botón eliminar
        holder.btnDelete.setOnClickListener {
            cartItems.removeAt(position)
            notifyItemRemoved(position)
            onCartUpdated()
        }
    }

    override fun getItemCount(): Int = cartItems.size
}
