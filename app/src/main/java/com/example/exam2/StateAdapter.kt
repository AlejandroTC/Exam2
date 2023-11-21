package com.example.exam2

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.exam2.room.Estado

class StateAdapter (private val listadoEstados: List<Estado>): RecyclerView.Adapter<StateAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StateAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.state_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: StateAdapter.ViewHolder, position: Int) {
        holder.nombreEstado.text = listadoEstados[position].nombre
        holder.idEstado.text = listadoEstados[position].stateId.toString()
        holder.itemView.setOnClickListener {
            val intentActualizar = Intent(holder.itemView.context, UpdateState::class.java)
            intentActualizar.putExtra("id", listadoEstados[position].stateId)
            intentActualizar.putExtra("nombre", listadoEstados[position].nombre)
            holder.itemView.context.startActivity(intentActualizar)
        }
    }

    override fun getItemCount(): Int {
        return listadoEstados.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreEstado: TextView = itemView.findViewById(R.id.stateName_tv)
        val idEstado: TextView = itemView.findViewById(R.id.stateId_tv)
    }
}
