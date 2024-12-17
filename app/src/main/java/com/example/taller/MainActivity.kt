package com.example.taller

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat



class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Obtener el token de SharedPreferences
        val sharedPreferences = getSharedPreferences("MiAppPrefs", MODE_PRIVATE)
        val token = sharedPreferences.getString("jwt_token", "No token found")


        findViewById<Button>(R.id.btnRegister).setOnClickListener {
            val registerIntent = Intent(this, RegisterActivity::class.java)
            startActivity(registerIntent)
        }


        findViewById<Button>(R.id.btnLogin).setOnClickListener {
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
        }

        findViewById<Button>(R.id.btnAdmin).setOnClickListener {
            val adminintent = Intent(this,CarListActivity::class.java)
            startActivity(adminintent)
        }



    }
}