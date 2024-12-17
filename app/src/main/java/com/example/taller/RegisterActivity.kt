package com.example.taller

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Referencias a los elementos de la vista
        val etName = findViewById<EditText>(R.id.etName)
        val etSurname = findViewById<EditText>(R.id.etSurname)
        val etNumberIdentification = findViewById<EditText>(R.id.etNumberIdentification)
        val etPhone = findViewById<EditText>(R.id.etPhone)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnRegister = findViewById<Button>(R.id.btnRegister)

        // Evento de clic del botón Registrar
        btnRegister.setOnClickListener {
            val name = etName.text.toString()
            val surname = etSurname.text.toString()
            val numberIdentification = etNumberIdentification.text.toString()
            val phone = etPhone.text.toString()
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            // Validación simple
            if (name.isEmpty() || surname.isEmpty() || numberIdentification.isEmpty() ||
                phone.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show()
            } else {
                // Llamar a la función para registrar el usuario
                registerUser(name, surname, numberIdentification, phone, email, password)
            }
        }
    }

    private fun registerUser(name: String, surname: String, numberIdentification: String, phone: String, email: String, password: String) {
        val url = "http://192.168.170.24:4000/api/auth/register" // Cambia localhost por 10.0.2.2 si estás usando un emulador

        val requestBody = JSONObject()
        requestBody.put("name", name)
        requestBody.put("surname", surname)
        requestBody.put("numberidentification", numberIdentification)
        requestBody.put("phone", phone)
        requestBody.put("email", email)
        requestBody.put("password", password)

        val requestQueue: RequestQueue = Volley.newRequestQueue(this)

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url, requestBody,
            Response.Listener { response ->
                try {
                    // Extraer el token del JSON de respuesta
                    val token = response.getString("token") // Asegúrate de que la API devuelve "token" en el JSON

                    // Guardar el token en SharedPreferences
                    guardarTokenEnPreferencias(token)

                    // Mostrar mensaje de éxito
                    Toast.makeText(this, "Usuario registrado con éxito.", Toast.LENGTH_SHORT).show()

                    // Abrir HomeActivity
                    abrirHomeActivity()
                } catch (e: Exception) {
                    Toast.makeText(this, "Error al procesar la respuesta.", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                try {
                    // Manejo del error
                    val statusCode = error.networkResponse?.statusCode
                    val errorBody = String(error.networkResponse?.data ?: ByteArray(0)) // Extraer cuerpo del error
                    val errorMessage = JSONObject(errorBody).optString("message", "Error desconocido")

                    // Mostrar mensaje de error específico
                    if (statusCode == 400) {
                        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Error: $errorMessage", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this, "Error al procesar el error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        )

        // Agregar la solicitud a la cola
        requestQueue.add(jsonObjectRequest)
    }

    // Función para guardar el token en SharedPreferences
    private fun guardarTokenEnPreferencias(token: String) {
        val sharedPreferences = getSharedPreferences("MiAppPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("jwt_token", token)
        editor.apply()
    }

    // Función para abrir HomeActivity
    private fun abrirHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish() // Cierra la actividad actual para evitar volver atrás
    }
}
