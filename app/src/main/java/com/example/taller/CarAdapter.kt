package com.example.taller

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CarAdapter(
    private val cars: List<Car>,
    private val onUpdateClick: (Car) -> Unit,
    private val onDeleteClick: (Car) -> Unit
) : RecyclerView.Adapter<CarAdapter.CarViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.car_item, parent, false)
        return CarViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        val car = cars[position]
        holder.bind(car)
    }

    override fun getItemCount(): Int = cars.size

    inner class CarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvCarName: TextView = itemView.findViewById(R.id.tvCarName)
        private val tvCarPrice: TextView = itemView.findViewById(R.id.tvCarPrice)
        private val tvCarStock: TextView = itemView.findViewById(R.id.tvCarStock)
        private val btnUpdateCar: Button = itemView.findViewById(R.id.btnUpdateCar)
        private val btnDeleteCar: Button = itemView.findViewById(R.id.btnDeleteCar)

        fun bind(car: Car) {
            tvCarName.text = car.name
            tvCarPrice.text = "Price: $${car.price}"
            tvCarStock.text = "Stock: ${car.stock}"

            btnUpdateCar.setOnClickListener { onUpdateClick(car) }
            btnDeleteCar.setOnClickListener { onDeleteClick(car) }
        }
    }
}
