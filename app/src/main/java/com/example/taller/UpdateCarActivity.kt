package com.example.taller



import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class UpdateCarActivity : AppCompatActivity() {

    private lateinit var etCarName: EditText
    private lateinit var etCarModel: EditText
    private lateinit var etCarBrand: EditText
    private lateinit var etCarPrice: EditText
    private lateinit var etCarStock: EditText
    private lateinit var etCarDescription: EditText
    private lateinit var btnUpdateCarSubmit: Button

    private var carId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_car)

        // Obtener referencias a las vistas
        etCarName = findViewById(R.id.etCarName)
        etCarModel = findViewById(R.id.etCarModel)
        etCarBrand = findViewById(R.id.etCarBrand)
        etCarPrice = findViewById(R.id.etCarPrice)
        etCarStock = findViewById(R.id.etCarStock)
        etCarDescription = findViewById(R.id.etCarDescription)
        btnUpdateCarSubmit = findViewById(R.id.btnUpdateCarSubmit)

        // Obtener el ID del carro desde el Intent
        carId = intent.getIntExtra("car_id", -1)

        if (carId == -1) {
            Toast.makeText(this, "Carro no válido", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Cargar los datos del carro
        loadCarDetails()

        // Configurar el botón para actualizar el carro
        btnUpdateCarSubmit.setOnClickListener { updateCar() }
    }

    private fun loadCarDetails() {
        val url = "http://192.168.170.24:4000/api/cars/$carId"

        val requestQueue = Volley.newRequestQueue(this)

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    etCarName.setText(response.getString("name"))
                    etCarModel.setText(response.getString("model"))
                    etCarBrand.setText(response.getString("brand"))
                    etCarPrice.setText(response.getDouble("price").toString())
                    etCarStock.setText(response.getInt("stock").toString())
                    etCarDescription.setText(response.getString("description"))
                } catch (e: Exception) {
                    Log.e("UpdateCarActivity", "Error parsing car details: ${e.message}")
                }
            },
            { error ->
                Log.e("UpdateCarActivity", "Error fetching car details: ${error.message}")
                Toast.makeText(this, "Error al cargar detalles del carro", Toast.LENGTH_SHORT).show()
            }
        )

        requestQueue.add(jsonObjectRequest)
    }

    private fun updateCar() {
        val url = "http://192.168.170.24:4000/api/cars/$carId"

        val updatedCar = JSONObject()
        updatedCar.put("name", etCarName.text.toString())
        updatedCar.put("model", etCarModel.text.toString())
        updatedCar.put("brand", etCarBrand.text.toString())
        updatedCar.put("price", etCarPrice.text.toString().toDoubleOrNull() ?: 0.0)
        updatedCar.put("stock", etCarStock.text.toString().toIntOrNull() ?: 0)
        updatedCar.put("description", etCarDescription.text.toString())

        val requestQueue = Volley.newRequestQueue(this)

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.PUT, url, updatedCar,
            { response ->
                Toast.makeText(this, "Carro actualizado correctamente", Toast.LENGTH_SHORT).show()
                finish() // Volver a la pantalla anterior
            },
            { error ->
                Log.e("UpdateCarActivity", "Error updating car: ${error.message}")
                Toast.makeText(this, "Error al actualizar el carro", Toast.LENGTH_SHORT).show()
            }
        )

        requestQueue.add(jsonObjectRequest)
    }
}
