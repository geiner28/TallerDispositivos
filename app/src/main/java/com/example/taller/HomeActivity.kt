package com.example.taller

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class HomeActivity : AppCompatActivity() {

    private val productList = mutableListOf<Product>()
    private val cartItems = mutableListOf<CartItem>() // Lista del carrito
    private lateinit var productAdapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val btnGoToCart = findViewById<Button>(R.id.btnGoToCart)
        btnGoToCart.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            intent.putParcelableArrayListExtra("cartItems", ArrayList(cartItems)) // Pasar el carrito correctamente
            startActivity(intent)
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        productAdapter = ProductAdapter(productList) { product, quantity ->
            addToCart(product, quantity)
        }
        recyclerView.adapter = productAdapter
    }

    override fun onResume() {
        super.onResume()
        // Limpia la lista antes de recargar productos
        productList.clear()
        fetchProducts(productAdapter)
    }

    private fun addToCart(product: Product, quantity: Int) {
        val existingItemIndex = cartItems.indexOfFirst { it.product == product }
        if (existingItemIndex != -1) {
            // Crear un nuevo objeto CartItem con la cantidad actualizada
            val existingItem = cartItems[existingItemIndex]
            val updatedItem = existingItem.copy(quantity = existingItem.quantity + quantity)
            cartItems[existingItemIndex] = updatedItem
        } else {
            // Si no está en el carrito, lo agregamos
            cartItems.add(CartItem(product, quantity))
        }
    }

    // Función para obtener productos desde la API
    private fun fetchProducts(adapter: ProductAdapter) {
        val url = "http://192.168.170.24:4000/api/cars" // Cambia localhost por 10.0.2.2 si usas un emulador
        val requestQueue = Volley.newRequestQueue(this)

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                for (i in 0 until response.length()) {
                    val jsonObject = response.getJSONObject(i)
                    val product = Product(
                        id = jsonObject.getInt("id"), // Obtener el ID del carro
                        name = jsonObject.getString("name"),
                        model = jsonObject.getString("model"),
                        brand = jsonObject.getString("brand"),
                        price = jsonObject.getDouble("price"),
                        description = jsonObject.getString("description"),
                        stock = jsonObject.getInt("stock"),
                        image = jsonObject.getString("image")
                    )
                    productList.add(product)
                }
                adapter.notifyDataSetChanged()
            },
            { error ->
                Toast.makeText(this, "Error al obtener productos: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        )

        requestQueue.add(jsonArrayRequest)
    }
}
