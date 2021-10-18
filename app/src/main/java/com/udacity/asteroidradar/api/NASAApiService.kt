package com.udacity.asteroidradar.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.ImageOfTheDay
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://api.nasa.gov"

/**
 * Build the Moshi object that Retrofit will be using, making sure to add the Kotlin adapter for
 * full Kotlin compatibility.
 */
private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

/**
 * Use the Retrofit builder to build a retrofit object using a Moshi converter with our Moshi
 * object.
 */
private val retrofit = Retrofit.Builder()
        .addConverterFactory(ScalarsConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()

private val retrofitImage = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(BASE_URL)
        .build()

/**
 * A public interface that exposes the [getProperties] method
 */
interface NASAApiService {
    /**
     * Returns a Coroutine [List] of [MarsProperty] which can be fetched with await() if in a Coroutine scope.
     * The @GET annotation indicates that the "realestate" endpoint will be requested with the GET
     * HTTP method
     */
    @GET("/neo/rest/v1/feed")
    fun getAsteroids(@Query("start_date") startDate: String, @Query("end_date") endDate: String, @Query("api_key") API_Key: String): Call<String>

    @GET("/planetary/apod")
    fun getImageOfTheDay(@Query("api_key") API_Key: String): Call<ImageOfTheDay>
}

/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object NASAApi {
    val retrofitService : NASAApiService by lazy { retrofit.create(NASAApiService::class.java) }
    val retrofitImageService : NASAApiService by lazy { retrofitImage.create(NASAApiService::class.java) }
}

