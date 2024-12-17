package com.example.taller

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

class CartActivity : AppCompatActivity() {

    private val cartItems = mutableListOf<CartItem>() // Lista del carrito
    private lateinit var cartAdapter: CartAdapter
    private lateinit var tvTotal: TextView // Total de la compra

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        // Configurar RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewCart)
        recyclerView.layoutManager = LinearLayoutManager(this)
        cartAdapter = CartAdapter(cartItems) {
            updateTotal() // Actualizar total cuando cambien los elementos del carrito
        }
        recyclerView.adapter = cartAdapter

        // Recibir datos del carrito desde HomeActivity
        val receivedCartItems = intent.getParcelableArrayListExtra<CartItem>("cartItems")
        if (receivedCartItems != null) {
            cartItems.addAll(receivedCartItems)
            cartAdapter.notifyDataSetChanged()
        }

        // Referencia al TextView de total
        tvTotal = findViewById(R.id.tvTotal)
        updateTotal() // Mostrar total inicial

        // Botón para vaciar el carrito
        val btnClearCart = findViewById<Button>(R.id.btnClearCart)
        btnClearCart.setOnClickListener {
            cartItems.clear()
            cartAdapter.notifyDataSetChanged()
            updateTotal() // Actualizar total
        }

        // Botón Finalizar Compra
        val btnFinalize = findViewById<Button>(R.id.btnFinalizeOrder)
        btnFinalize.setOnClickListener {
            finalizeOrder()
        }
    }

    private fun updateTotal() {
        // Calcula el total sumando los precios de los productos por su cantidad
        val total = cartItems.sumOf { it.product.price * it.quantity }
        tvTotal.text = "Total: $%.2f".format(total)
    }

    private fun finalizeOrder() {
        // Obtener datos del formulario
        val etClientName = findViewById<EditText>(R.id.etClientName)
        val etClientID = findViewById<EditText>(R.id.etClientID)
        val etPhone = findViewById<EditText>(R.id.etPhone)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etAddress = findViewById<EditText>(R.id.etAddress)

        val clientName = etClientName.text.toString().trim()
        val clientID = etClientID.text.toString().trim()
        val phone = etPhone.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val address = etAddress.text.toString().trim()

        // Validar campos
        if (clientName.isEmpty() || clientID.isEmpty() || phone.isEmpty() || email.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        // Crear el JSON para la orden
        val orderJson = JSONObject().apply {
            put("clientName", clientName)
            put("clientID", clientID)
            put("phone", phone)
            put("email", email)
            put("address", address)
            put("cars", JSONArray().apply {
                cartItems.forEach { item ->
                    put(JSONObject().apply {
                        put("id", item.product.id) // Incluir el ID del producto
                        put("quantity", item.quantity)
                    })
                }
            })
            put("total", cartItems.sumOf { it.product.price * it.quantity })
        }

        // Enviar la orden a la API
        sendOrderToApi(orderJson)
    }

    private fun sendOrderToApi(orderJson: JSONObject) {
        val url = "http://192.168.170.24:4000/api/orders/" // Cambia localhost por 10.0.2.2 si usas un emulador
        val request = JsonObjectRequest(
            Request.Method.POST, url, orderJson,
            { response ->
                Toast.makeText(this, "Orden enviada exitosamente", Toast.LENGTH_SHORT).show()
                cartItems.clear()
                cartAdapter.notifyDataSetChanged()
                updateTotal() // Actualizar total
                val intent = Intent(this, OrderConfirmationActivity::class.java)
                intent.putExtra("orderDetails", response.toString()) // Pasar los detalles de la orden
                startActivity(intent)
                finish()

            },
            { error ->
                Toast.makeText(this, "Error al enviar la orden: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        )

        Volley.newRequestQueue(this).add(request)
    }
}

