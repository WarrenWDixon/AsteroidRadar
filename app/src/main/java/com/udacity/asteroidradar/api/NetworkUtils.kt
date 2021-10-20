package com.udacity.asteroidradar.api

import android.util.Log
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

fun parseAsteroidsJsonResult(jsonResult: JSONObject): ArrayList<Asteroid> {
    Log.d("WWD", "in parse jsonResult:  ")
    val nearEarthObjectsJson = jsonResult.getJSONObject("near_earth_objects")

    val asteroidList = ArrayList<Asteroid>()

    val nextSevenDaysFormattedDates = getNextSevenDaysFormattedDates()
    var dateAsteroidJsonArray : JSONArray
    for (formattedDate in nextSevenDaysFormattedDates) {

        try {
            dateAsteroidJsonArray = nearEarthObjectsJson.getJSONArray(formattedDate)
        } catch (e: Exception){
            Log.d("WWD", "no array for date" + formattedDate)
            continue;
        }
       //Log.d("WWD", "dateArray is " + dateAsteroidJsonArray)
         for (i in 0 until dateAsteroidJsonArray.length()) {
            val asteroidJson = dateAsteroidJsonArray.getJSONObject(i)
            val id = asteroidJson.getLong("id")
            val codename = asteroidJson.getString("name")
            val absoluteMagnitude = asteroidJson.getDouble("absolute_magnitude_h")
            val estimatedDiameter = asteroidJson.getJSONObject("estimated_diameter")
                .getJSONObject("kilometers").getDouble("estimated_diameter_max")

            val closeApproachData = asteroidJson
                .getJSONArray("close_approach_data").getJSONObject(0)
            val relativeVelocity = closeApproachData.getJSONObject("relative_velocity")
                .getDouble("kilometers_per_second")
            val distanceFromEarth = closeApproachData.getJSONObject("miss_distance")
                .getDouble("astronomical")
            val isPotentiallyHazardous = asteroidJson
                .getBoolean("is_potentially_hazardous_asteroid")

            val asteroid = Asteroid(id, codename, formattedDate, absoluteMagnitude,
                estimatedDiameter, relativeVelocity, distanceFromEarth, isPotentiallyHazardous)
            asteroidList.add(asteroid)
        }
    }
    Log.d("WWD", "in parse asteroidList size is " + asteroidList.size)
    return asteroidList
}

private fun getNextSevenDaysFormattedDates(): ArrayList<String> {
    val formattedDateList = ArrayList<String>()
    val calendar = Calendar.getInstance()
    for (i in 0..Constants.DEFAULT_END_DATE_DAYS) {
        val currentTime = calendar.time
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        formattedDateList.add(dateFormat.format(currentTime))
        calendar.add(Calendar.DAY_OF_YEAR, 1)
    }

    return formattedDateList
}

fun computeEndDate() : String {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_YEAR, 1)
    val currentTime = calendar.time
    val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
    return dateFormat.format(currentTime)
}

    // THE FOLLOWING CODE WORKS AND READS THE ASTEROID DATA SINCE
   /* fun getNASAAsteroids() : MutableList<Asteroid> {
    var NASAresponse: String? = null
    val currentDate: String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    val endDate = computeEndDate();
    var asteroidList = mutableListOf<Asteroid>()
    NASAApi.retrofitService.getAsteroids(currentDate, endDate, Constants.API_KEY).enqueue( object:
        Callback<String> {

        override fun onFailure(call: Call<String>, t: Throwable) {
            NASAresponse = "Failure: " + t.message
            Log.d("WWD", "API call failed  " + NASAresponse)
        }

        override fun onResponse(call: Call<String>, response: Response<String>) {
            NASAresponse = response.body().toString()
            Log.d("WWD", " API call success ")
            if (NASAresponse != null) {
                val myJSON = JSONObject(NASAresponse!!)
                asteroidList = parseAsteroidsJsonResult(myJSON)
                //Log.d("WWD", "in getAsteroids parsed " + asteroidList)
            }
        }
    })
    return asteroidList
} */