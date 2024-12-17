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

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Referencias a los elementos de la vista
        val etEmail = findViewById<EditText>(R.id.etLoginEmail)
        val etPassword = findViewById<EditText>(R.id.etLoginPassword)
        val btnLogin = findViewById<Button>(R.id.btnLoginSubmit)

        // Evento de clic en el botón de inicio de sesión
        btnLogin.setOnClickListener {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            // Validación básica
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show()
            } else {
                // Llamar a la función para iniciar sesión
                loginUser(email, password)
            }
        }
    }

    private fun loginUser(email: String, password: String) {
        val url = "http://192.168.170.24:4000/api/auth/login" // Cambia localhost por 10.0.2.2 si estás en un emulador

        val requestBody = JSONObject()
        requestBody.put("email", email)
        requestBody.put("password", password)

        val requestQueue: RequestQueue = Volley.newRequestQueue(this)

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url, requestBody,
            Response.Listener { response ->
                try {
                    // Extraer el token de la respuesta
                    val token = response.getString("token") // Asegúrate de que el servidor devuelve un campo "token"

                    // Guardar el token en SharedPreferences
                    guardarTokenEnPreferencias(token)

                    // Mostrar mensaje de éxito
                    Toast.makeText(this, "Inicio de sesión exitoso.", Toast.LENGTH_SHORT).show()

                    // Redirigir a HomeActivity
                    abrirHomeActivity()
                } catch (e: Exception) {
                    Toast.makeText(this, "Error al procesar la respuesta.", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                // Manejo del error
                try {
                    val statusCode = error.networkResponse?.statusCode
                    val errorBody = String(error.networkResponse?.data ?: ByteArray(0)) // Extraer cuerpo del error
                    val errorMessage = JSONObject(errorBody).optString("message", "Error desconocido")

                    // Mostrar mensaje de error específico
                    if (statusCode == 401) {
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
        finish() // Cierra LoginActivity para evitar volver atrás
    }
}
