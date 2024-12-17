package com.example.taller

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class OrderAdapter(
    private val orders: List<Order>
) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.order_item, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]
        holder.bind(order)
    }

    override fun getItemCount(): Int = orders.size

    inner class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvClientName: TextView = itemView.findViewById(R.id.tvClientName)
        private val tvOrderTotal: TextView = itemView.findViewById(R.id.tvOrderTotal)
        private val tvCarsDetails: TextView = itemView.findViewById(R.id.tvCarsDetails)

        fun bind(order: Order) {
            tvClientName.text = order.clientName
            tvOrderTotal.text = "Total: \$${order.total}"

            // Mostrar los carros comprados
            val carsDetails = StringBuilder()
            for (car in order.cars) {
                carsDetails.append("Car: ${car.name} | Brand: ${car.brand} | Price: \$${car.price} | Quantity: ${car.stock}\n")
            }
            tvCarsDetails.text = carsDetails.toString()
        }
    }
}
