package com.example.taller

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley

class CarListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var carAdapter: CarAdapter
    private lateinit var carList: MutableList<Car>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_list)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        carList = mutableListOf()

        // Configura el adaptador con los listeners de actualizar y eliminar
        carAdapter = CarAdapter(carList, ::onUpdateClick, ::onDeleteClick)
        recyclerView.adapter = carAdapter

        fetchCars()


        findViewById<Button>(R.id.btnAddCar).setOnClickListener {
            val intent = Intent(this, CreateCarActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.btnOrder).setOnClickListener {
            val intent = Intent(this, OrderListActivity::class.java)
            startActivity(intent)
        }



    }

    override fun onResume() {
        super.onResume()
        // Recargar los carros cuando la actividad vuelve a primer plano
        fetchCars()
    }

    private fun fetchCars() {
        val url = "http://192.168.170.24:4000/api/cars"  // IP de tu API

        val requestQueue = Volley.newRequestQueue(this)

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                carList.clear()
                for (i in 0 until response.length()) {
                    val carJson = response.getJSONObject(i)
                    val car = Car(
                        id = carJson.getInt("id"),
                        name = carJson.getString("name"),
                        model = carJson.getString("model"),
                        brand = carJson.getString("brand"),
                        price = carJson.getDouble("price"),
                        description = carJson.getString("description"),
                        stock = carJson.getInt("stock"),
                        image = carJson.getString("image")
                    )
                    carList.add(car)
                }
                carAdapter.notifyDataSetChanged()
            },
            { error ->
                Log.e("CarListActivity", "Error al obtener los carros: ${error.message}")
                Toast.makeText(this, "Error al cargar la lista", Toast.LENGTH_SHORT).show()
            }
        )

        requestQueue.add(jsonArrayRequest)
    }



    private fun onUpdateClick(car: Car) {
        // Navegar a la pantalla de actualización de carro
        val intent = Intent(this, UpdateCarActivity::class.java)
        intent.putExtra("car_id", car.id)
        startActivity(intent)
    }

    private fun onDeleteClick(car: Car) {
        val url = "http://192.168.170.24:4000/api/cars/${car.id}"  // Endpoint para eliminar el carro

        val requestQueue = Volley.newRequestQueue(this)

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.DELETE, url, null,
            { response ->
                Toast.makeText(this, "Carro eliminado correctamente", Toast.LENGTH_SHORT).show()
                carList.remove(car) // Eliminar el carro localmente
                carAdapter.notifyDataSetChanged()
            },
            { error ->
                Log.e("CarListActivity", "Error al eliminar el carro: ${error.message}")
                Toast.makeText(this, "Error al eliminar el carro", Toast.LENGTH_SHORT).show()
            }
        )

        requestQueue.add(jsonObjectRequest)
    }


}
