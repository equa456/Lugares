package com.lugares.repository

import androidx.lifecycle.LiveData
import com.lugares.data.LugarDao
import com.lugares.model.Lugar

class LugarRepository (private val lugarDao: LugarDao) {
    //Se implementan las funciones de la interface

    //Se crea un objeto que contiene el arrayList de los registros de la tabla lugar... cubiertos por LiveData
    val getAllData: LiveData<List<Lugar>> = lugarDao.getAllData()

    //se define la funcion para insertar un l;ugar en la tabla
    suspend fun addLugar(lugar: Lugar){
        lugarDao.addLugar(lugar)
    }

    //se define la funcion para actualizar un l;ugar en la tabla
    suspend fun updateLugar(lugar: Lugar){
        lugarDao.updateLugar(lugar)
    }

    //se define la funcion para eliminar un l;ugar en la tabla
    suspend fun deeleteLugar(lugar: Lugar){
        lugarDao.deleteLugar(lugar)
    }

}