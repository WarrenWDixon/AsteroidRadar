package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.database.getDatabase

class MainViewModel(application: Application) : AndroidViewModel(application) {


    private val _theAsteroid = MutableLiveData<Asteroid>()
    val theAsteroid
        get() = _theAsteroid

    fun setTheAsteroid(anAsteroid: Asteroid)  {
        _theAsteroid.value = anAsteroid
    }

    //private val database = getDatabase()

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