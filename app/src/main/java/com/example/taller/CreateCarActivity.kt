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

class CreateCarActivity : AppCompatActivity() {

    private lateinit var etCarName: EditText
    private lateinit var etCarModel: EditText
    private lateinit var etCarBrand: EditText
    private lateinit var etCarPrice: EditText
    private lateinit var etCarStock: EditText
    private lateinit var etCarDescription: EditText
    private lateinit var btnCreateCarSubmit: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_car)

        // Inicializar las vistas
        etCarName = findViewById(R.id.etCarName)
        etCarModel = findViewById(R.id.etCarModel)
        etCarBrand = findViewById(R.id.etCarBrand)
        etCarPrice = findViewById(R.id.etCarPrice)
        etCarStock = findViewById(R.id.etCarStock)
        etCarDescription = findViewById(R.id.etCarDescription)
        btnCreateCarSubmit = findViewById(R.id.btnCreateCarSubmit)

        // Configurar el botón de enviar
        btnCreateCarSubmit.setOnClickListener {
            createCar()
        }
    }

    private fun createCar() {
        val url = "http://192.168.170.24:4000/api/cars"

        // Crear un objeto JSON con los datos del formulario
        val newCar = JSONObject()
        newCar.put("name", etCarName.text.toString())
        newCar.put("model", etCarModel.text.toString())
        newCar.put("brand", etCarBrand.text.toString())
        newCar.put("price", etCarPrice.text.toString().toDoubleOrNull() ?: 0.0)
        newCar.put("stock", etCarStock.text.toString().toIntOrNull() ?: 0)
        newCar.put("description", etCarDescription.text.toString())

        val requestQueue = Volley.newRequestQueue(this)

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url, newCar,
            { response ->
                Toast.makeText(this, "Carro creado correctamente", Toast.LENGTH_SHORT).show()
                finish() // Regresar a la actividad anterior
            },
            { error ->
                Log.e("CreateCarActivity", "Error al crear el carro: ${error.message}")
                Toast.makeText(this, "Error al crear el carro", Toast.LENGTH_SHORT).show()
            }
        )

        requestQueue.add(jsonObjectRequest)
    }
}
