package com.example.exam2

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.exam2.room.Persona

class PersonAdapter (private val listadoPersona: List<Persona>):RecyclerView.Adapter<PersonAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.person_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: PersonAdapter.ViewHolder, position: Int) {
        holder.nombrePersona.text = listadoPersona[position].nombre
        holder.edadPersona.text = listadoPersona[position].edad.toString()
        holder.itemView.setOnClickListener{
            val intentActualizar = Intent(holder.itemView.context, UpdatePerson::class.java)
            intentActualizar.putExtra("id", listadoPersona[position].personId)
            intentActualizar.putExtra("nombre", listadoPersona[position].nombre)
            intentActualizar.putExtra("edad", listadoPersona[position].edad)
            holder.itemView.context.startActivity(intentActualizar)
        }
    }

    override fun getItemCount(): Int {
        return listadoPersona.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val nombrePersona: TextView = itemView.findViewById(R.id.name_tv)
        val edadPersona: TextView = itemView.findViewById(R.id.age_tv)
    }

}