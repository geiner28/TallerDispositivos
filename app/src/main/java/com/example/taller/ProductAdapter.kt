package com.example.taller
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ProductAdapter(
    private val productList: List<Product>,
    private val onAddToCart: (Product, Int) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivProductImage: ImageView = itemView.findViewById(R.id.ivProductImage)
        val tvProductName: TextView = itemView.findViewById(R.id.tvProductName)
        val tvProductPrice: TextView = itemView.findViewById(R.id.tvProductPrice)
        val tvQuantity: TextView = itemView.findViewById(R.id.tvQuantity)
        val btnIncrease: Button = itemView.findViewById(R.id.btnIncrease)
        val btnDecrease: Button = itemView.findViewById(R.id.btnDecrease)
        val btnAddToCart: Button = itemView.findViewById(R.id.btnAddToCart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        var quantity = 1

        holder.tvProductName.text = product.name
        holder.tvProductPrice.text = "Precio: $${product.price}"
        holder.tvQuantity.text = quantity.toString()

        // Cargar la imagen con Glide
        Glide.with(holder.itemView.context)
            .load(product.image) // URL de la imagen

            .into(holder.ivProductImage) // Asignar al ImageView

        // Botón para incrementar cantidad
        holder.btnIncrease.setOnClickListener {
            if (quantity < product.stock) {
                quantity++
                holder.tvQuantity.text = quantity.toString()
            } else {
                Toast.makeText(holder.itemView.context, "Stock insuficiente", Toast.LENGTH_SHORT).show()
            }
        }

        // Botón para decrementar cantidad
        holder.btnDecrease.setOnClickListener {
            if (quantity > 1) {
                quantity--
                holder.tvQuantity.text = quantity.toString()
            }
        }

        // Botón para agregar al carrito
        holder.btnAddToCart.setOnClickListener {
            onAddToCart(product, quantity)
            Toast.makeText(holder.itemView.context, "Producto añadido al carrito", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int = productList.size
}
