package com.example.exam2.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Estado(
    @PrimaryKey(autoGenerate = true) val stateId:Long=0,
    @ColumnInfo(name = "nombre") val nombre: String
)