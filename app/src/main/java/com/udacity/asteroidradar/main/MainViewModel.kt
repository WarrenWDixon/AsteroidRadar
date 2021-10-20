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
    //private val asteroidRepository = AsteroidRepository(database)
    //val asteroidList = asteroidRepository.asteroids
    /* init {
        viewModelScope.launch {
            //asteroidRepository.refreshAsteroids()
            viewModelGetAsteroids();
        }
    } */

    // test logic to use mars 8 type network logic
    private val _asteroids = MutableLiveData<List<Asteroid>>()
    val asteroids: LiveData<List<Asteroid>>
        get() = _asteroids

    fun viewModelGetAsteroids() {
        val currentDate: String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val endDate = computeEndDate();
       // val asteroidList = getNASAAsteroids()
       // Log.d("WWD", "view model asteroidlist is " + asteroidList)

        // THE FOLLOWING CODE IS HOW MARS STEP 8 WORKS
         /* viewModelScope.launch {
            try {
               _asteroids.value = NASAApi.retrofitService.getAsteroids(currentDate, endDate, Constants.API_KEY)
                Log.d("WWD", "view model asteroidlist is " + asteroidlist)
            } catch (e: Exception) {
                //_asteroids.value = ArrayList()
                Log.d("WWD", "view model network exception " + e.message)
            }
        } */
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