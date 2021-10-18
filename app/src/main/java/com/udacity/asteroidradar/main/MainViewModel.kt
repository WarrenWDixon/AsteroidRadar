package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.database.getDatabase

class MainViewModel : ViewModel() {


    private val _theAsteroid = MutableLiveData<Asteroid>()
    val theAsteroid
        get() = _theAsteroid

    fun setTheAsteroid(anAsteroid: Asteroid)  {
        _theAsteroid.value = anAsteroid
    }

    private val database = getDatabase()
}