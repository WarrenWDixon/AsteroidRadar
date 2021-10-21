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
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
//import com.udacity.asteroidradar.api.getNASAAsteroids
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.database.asDatabaseModel
import com.udacity.asteroidradar.database.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
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
        val currentDate: String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val endDate = computeEndDate();
        withContext(Dispatchers.IO) {
            try {
                // following line of code used getNASAAsteroids in network utils, but it returns before the network data is fetched so returns empty array
                val asteroidList = parseAsteroidsJsonResult(
                    JSONObject(
                        retrofitService.getAsteroids(
                            currentDate,
                            endDate,
                            Constants.API_KEY
                        )
                    )
                )
                database.asteroidDao.insertAll(*asteroidList.asDatabaseModel())
            } catch (e: Exception) {
                Log.d("WWD", "refreshAsteroids exception:" + e.message)
            }
        }
    }
}