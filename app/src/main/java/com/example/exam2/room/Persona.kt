package com.example.exam2.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Persona(
    @PrimaryKey(autoGenerate = true) val personId:Int=0,
    @ColumnInfo(name = "nombre") val nombre: String,
    @ColumnInfo(name = "edad") val edad: Int=0,
    @ColumnInfo(name = "id_estado") val stateId:Long
)