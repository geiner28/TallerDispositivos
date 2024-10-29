package com.example.taller

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val emailInput = findViewById<EditText>(R.id.etLoginEmail)
        val passwordInput = findViewById<EditText>(R.id.etLoginPassword)

        findViewById<Button>(R.id.btnLoginSubmit).setOnClickListener {
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()

            val user = RegisterActivity.userList.find { it.email == email && it.password == password }

            if (user != null) {
                startActivity(Intent(this, HomeActivity::class.java))
            } else {
                Toast.makeText(this, "Email o contraseña incorrectos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
