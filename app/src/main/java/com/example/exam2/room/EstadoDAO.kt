package com.example.exam2.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface EstadoDAO {
    @Query("SELECT * FROM estado")
    fun getAllEstados(): List<Estado>

    @Query("SELECT * FROM estado WHERE stateId = :idEstado")
    fun getEstadoById(idEstado: Long): Estado

    @Query("SELECT * FROM estado WHERE nombre = :nombreEstado")
    fun getEstadoByName(nombreEstado: String): Estado

    @Insert
    fun insertEstado(estado: Estado)

    @Update
    fun updateEstado(estado: Estado)

    @Delete
    fun deleteEstado(estado: Estado)
}