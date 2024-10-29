package com.example.taller

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {

    companion object {
        val userList = mutableListOf<User>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val nameInput = findViewById<EditText>(R.id.etName)
        val lastNameInput = findViewById<EditText>(R.id.etLastName)
        val idInput = findViewById<EditText>(R.id.etId)
        val emailInput = findViewById<EditText>(R.id.etEmail)
        val passwordInput = findViewById<EditText>(R.id.etPassword)

        findViewById<Button>(R.id.btnRegisterSubmit).setOnClickListener {
            val name = nameInput.text.toString()
            val lastName = lastNameInput.text.toString()
            val id = idInput.text.toString()
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()

            if (name.isNotBlank() && lastName.isNotBlank() && id.isNotBlank() && email.isNotBlank() && password.isNotBlank()) {
                userList.add(User(name, lastName, id, email, password))
                Toast.makeText(this, "Registrado con éxito", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<Button>(R.id.btnBack).setOnClickListener {
            finish()
        }
    }
}
