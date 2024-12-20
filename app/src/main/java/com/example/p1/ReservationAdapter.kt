package com.example.p1

import Service
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide // Import explicite pour Glide
class ReservationAdapter(
    private val services: MutableList<Service>,
    private val onReserveClick: (Service) -> Unit  // Une fonction callback pour gérer la réservation
) : RecyclerView.Adapter<ReservationAdapter.ReservationViewHolder>() {

    class ReservationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val serviceName: TextView = itemView.findViewById(R.id.serviceName)
        val serviceDescription: TextView = itemView.findViewById(R.id.serviceDescription)
        val servicePrice: TextView = itemView.findViewById(R.id.servicePrice)
        val serviceImage: ImageView = itemView.findViewById(R.id.serviceImage)

        fun bind(service: Service, onReserveClick: (Service) -> Unit) {
            serviceName.text = service.serviceName
            serviceDescription.text = service.description
            servicePrice.text = "Prix : ${service.price}"

            // Utiliser Glide ou une autre méthode pour charger l'image du service
            Glide.with(itemView.context)
                .load(service.imageUrl)
                .placeholder(R.drawable.logo_neto)
                .error(R.drawable.logo_neto)
                .into(serviceImage)

            // Définir l'action du bouton de réservation

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_service_reservation, parent, false)
        return ReservationViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReservationViewHolder, position: Int) {
        val service = services[position]
        holder.bind(service, onReserveClick)
    }

    override fun getItemCount(): Int = services.size
}
