package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.NASAApi
import com.udacity.asteroidradar.api.NASAApi.retrofitService
import com.udacity.asteroidradar.api.NASAApiService
import com.udacity.asteroidradar.api.computeEndDate
//import com.udacity.asteroidradar.api.getNASAAsteroids
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val _theAsteroid = MutableLiveData<Asteroid>()
    val theAsteroid
        get() = _theAsteroid

    fun setTheAsteroid(anAsteroid: Asteroid)  {
        _theAsteroid.value = anAsteroid
    }

    private val database = getDatabase(application)
    private val asteroidRepository = AsteroidRepository(database)
    init {
        viewModelScope.launch {
            asteroidRepository.refreshAsteroids()
        }
    }

    val asteroids: LiveData<List<Asteroid>> = asteroidRepository.asteroids

    fun displayAsteroidDetailCompleted() {
        theAsteroid.value = null
    }
    /**
     * Factory for constructing DevByteViewModel with parameter
     */
    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}