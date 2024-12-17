package com.example.taller

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject

class OrderConfirmationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_confirmation)

        // Referencias a los elementos del diseño
        val tvOrderId = findViewById<TextView>(R.id.tvOrderId)
        val tvClientName = findViewById<TextView>(R.id.tvClientName)
        val tvTotal = findViewById<TextView>(R.id.tvTotal)

        val btnExit = findViewById<Button>(R.id.btnExit)

        // Obtener los detalles de la orden desde el intent
        val orderDetails = intent.getStringExtra("orderDetails")
        if (orderDetails != null) {
            val orderJson = JSONObject(orderDetails) // Cambiado de JSONArray a JSONObject

            // Mostrar los datos principales de la orden
            tvOrderId.text = "ID de la orden: ${orderJson.getInt("id")}"
            tvClientName.text = "Cliente: ${orderJson.getString("clientName")}"
            tvTotal.text = "Total: $${orderJson.getString("total")}"

            // Verificar si contiene productos y mostrarlos (si los hay)

            // Botón para salir y volver a HomeActivity
            btnExit.setOnClickListener {
                val intent = Intent(this, HomeActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
        }

    }
}