package com.udacity.asteroidradar.repository

import android.net.Network
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.NASAApi
import com.udacity.asteroidradar.api.NASAApi.retrofitService
import com.udacity.asteroidradar.api.asDatabaseModel
import com.udacity.asteroidradar.api.computeEndDate
//import com.udacity.asteroidradar.api.getNASAAsteroids
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.database.asDatabaseModel
import com.udacity.asteroidradar.database.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class AsteroidRepository(private val database: AsteroidsDatabase) {
    val currentDate: String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    val endDate = computeEndDate();
    val asteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAsteroids(currentDate)) {
            it.asDomainModel()
        }

    suspend fun refreshAsteroids() {
        Log.d("WWD", "in repository refreshAsteroids")
        val currentDate: String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val endDate = computeEndDate();
        withContext(Dispatchers.IO) {
            // following line of code used getNASAAsteroids in network utils, but it returns before the network data is fetched so returns empty array
            // val asteroidList = getNASAAsteroids()
            Log.d("WWD", "before network call")
            // THE FOLLOWING LINE OF CODE CRASHES
            //val asteroidList = NASAApi.retrofitService.getAsteroids(currentDate, endDate, Constants.API_KEY).await()
            Log.d("WWD", "after network call")
           // Log.d("WWD", "in refreshAsteroids the data is " + asteroidList)
           // database.asteroidDao.insertAll(*asteroidList.asDatabaseModel())
        } */
    }
}