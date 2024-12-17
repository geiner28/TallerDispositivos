package com.example.taller

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley

class OrderListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var orderAdapter: OrderAdapter
    private lateinit var orderList: MutableList<Order>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_list)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        orderList = mutableListOf()

        orderAdapter = OrderAdapter(orderList)
        recyclerView.adapter = orderAdapter

        fetchOrders()
    }

    private fun fetchOrders() {
        val url = "http://192.168.170.24:4000/api/orders"  // URL de la API

        val requestQueue = Volley.newRequestQueue(this)

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                orderList.clear()
                for (i in 0 until response.length()) {
                    val orderJson = response.getJSONObject(i)
                    val carsList = mutableListOf<OrderCar>()
                    val carsArray = orderJson.getJSONArray("Cars")

                    for (j in 0 until carsArray.length()) {
                        val carJson = carsArray.getJSONObject(j)
                        val car = OrderCar(
                            id = carJson.getInt("id"),
                            name = carJson.getString("name"),
                            model = carJson.getString("model"),
                            brand = carJson.getString("brand"),
                            price = carJson.getString("price"),
                            description = carJson.getString("description"),
                            stock = carJson.getInt("stock"),
                            image = carJson.optString("image", null)
                        )
                        carsList.add(car)
                    }

                    val order = Order(
                        id = orderJson.getInt("id"),
                        clientName = orderJson.getString("clientName"),
                        clientID = orderJson.getString("clientID"),
                        phone = orderJson.getString("phone"),
                        email = orderJson.getString("email"),
                        address = orderJson.getString("address"),
                        total = orderJson.getString("total"),
                        createdAt = orderJson.getString("createdAt"),
                        updatedAt = orderJson.getString("updatedAt"),
                        cars = carsList
                    )
                    orderList.add(order)
                }
                orderAdapter.notifyDataSetChanged()
            },
            { error ->
                Log.e("OrderListActivity", "Error al obtener las órdenes: ${error.message}")
                Toast.makeText(this, "Error al cargar la lista de órdenes", Toast.LENGTH_SHORT).show()
            }
        )

        requestQueue.add(jsonArrayRequest)
    }
}
