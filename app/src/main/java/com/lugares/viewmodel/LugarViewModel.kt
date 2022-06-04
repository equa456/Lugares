package com.lugares.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.lugares.data.LugarDatabase
import com.lugares.model.Lugar
import com.lugares.repository.LugarRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LugarViewModel(application: Application) : AndroidViewModel(application) {

    val getAllData: LiveData<List<Lugar>>

    //Esta es la manera como accedo al repositorio desde el viewModel
    private val repository: LugarRepository

    //Se procede a inicializar los atributos de arriba de esta clase LugarVieMopdel

    init {
        val lugarDAo = LugarDatabase.getDatabase(application).lugarDao()
        repository = LugarRepository(lugarDAo)
        getAllData =  repository.getAllData
    }

    //Esta funcion de alto nivel llama al subproceso de I/O para grabar un registro lugar
    fun addLugar(lugar: Lugar){
        viewModelScope.launch(Dispatchers.IO){
            repository.addLugar(lugar)
        }
    }

    //Esta funcion de alto nivel llama al subproceso de I/O para actualizar un registro lugar
    fun updateLugar(lugar: Lugar){
        viewModelScope.launch(Dispatchers.IO){
            repository.updateLugar(lugar)
        }
    }

    //Esta funcion de alto nivel llama al subproceso de I/O para eliminar un registro lugar
    fun deleteLugar(lugar: Lugar){
        viewModelScope.launch(Dispatchers.IO){
            repository.deeleteLugar(lugar)
        }
    }
}