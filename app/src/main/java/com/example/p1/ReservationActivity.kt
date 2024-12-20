package com.example.p1

import Service
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReservationActivity : AppCompatActivity() {

    private lateinit var reservationAdapter: ReservationAdapter
    private val serviceList = mutableListOf<Service>()
    private val filteredServiceList = mutableListOf<Service>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservation)

        val recyclerView: RecyclerView = findViewById(R.id.servicesRecyclerView)
        val errorTextView: TextView = findViewById(R.id.errorTextView)

        // Initialisation de l'adaptateur avec la fonction de clic pour la réservation
        reservationAdapter = ReservationAdapter(filteredServiceList) { service ->
            onReserveClick(service)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = reservationAdapter

        loadServices(recyclerView, errorTextView)
    }

    private fun loadServices(recyclerView: RecyclerView, errorTextView: TextView) {
        val apiService = ApiClient.retrofit.create(ApiService::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val services = apiService.getAllServices()
                withContext(Dispatchers.Main) {
                    serviceList.clear()
                    serviceList.addAll(services)
                    filteredServiceList.clear()
                    filteredServiceList.addAll(services)
                    reservationAdapter.notifyDataSetChanged()
                    recyclerView.visibility = View.VISIBLE
                    errorTextView.visibility = View.GONE
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    errorTextView.text = "Erreur : ${e.message}"
                    errorTextView.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                }
            }
        }
    }

    private fun onReserveClick(service: Service) {
        // Logique de réservation, par exemple afficher un message ou démarrer une nouvelle activité
        Toast.makeText(this, "Service réservé : ${service.serviceName}", Toast.LENGTH_SHORT).show()

        // Exemple pour démarrer une nouvelle activité (si besoin)
        // val intent = Intent(this, ConfirmationActivity::class.java)
        // intent.putExtra("SERVICE", service)
        // startActivity(intent)
    }
}
