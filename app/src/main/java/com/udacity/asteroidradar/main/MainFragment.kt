package com.udacity.asteroidradar.main

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.*
import com.udacity.asteroidradar.api.NASAApi
import com.udacity.asteroidradar.api.computeEndDate
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.detail.DetailFragment
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class MainFragment : Fragment() {
    private var NASAresponse: String? = null
    private val API_KEY = "cLnzdGQHY2ooiBemGakHwkR71d8TPylFtLMuP7Nw"
    private var asteroidArray = mutableListOf<Asteroid>()

   /*private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    } */

    private val viewModel: MainViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onViewCreated()"
        }
        //The ViewModelProviders (plural) is deprecated.
        //ViewModelProviders.of(this, DevByteViewModel.Factory(activity.application)).get(DevByteViewModel::class.java)
        ViewModelProvider(this, MainViewModel.Factory(activity.application)).get(MainViewModel::class.java)

    }

    private lateinit var  binding2 : FragmentMainBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        Log.d("WWD", "in onCreateView for MainFragment")

        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        setHasOptionsMenu(true)
        getNASAAsteroids(binding)
        fetchImageOfTheDay(binding)
        viewModel.theAsteroid.observe(viewLifecycleOwner, { selectedAsteroid ->
            this.findNavController().navigate(MainFragmentDirections.actionShowDetail(selectedAsteroid))
        })

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }

    private fun getNASAAsteroids(binding: FragmentMainBinding) {
        val currentDate: String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val endDate = computeEndDate();
        Log.d("WWD", "end date is " + endDate)
        NASAApi.retrofitService.getAsteroids(currentDate, endDate, API_KEY).enqueue( object: Callback<String> {

            override fun onFailure(call: Call<String>, t: Throwable) {
                NASAresponse = "Failure: " + t.message
                Log.d("WWD", "API call failed  " + NASAresponse)
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response != null)
                    NASAresponse = response.body().toString()
                else
                    Log.d("WWD", "response is null")
                Log.d("WWD", " API call success ")
                if (NASAresponse != null) {
                    Log.d("WWD", "last char is " + NASAresponse.toString().last())
                    Log.d("WWD", "calling parse function")
                    val myJSON = JSONObject(NASAresponse!!)
                    //Log.d("WWD", "myJSON is " + myJSON.toString())
                    asteroidArray = parseAsteroidsJsonResult(myJSON)
                    Log.d("WWD", "after parse function")
                    Log.d("WWD"," asteroid list size is " + asteroidArray.size)
                    val asteroidAdapter = AsteroidAdapter(asteroidArray, AsteroidClickListener { anAsteroid ->
                        Snackbar.make(
                            requireActivity().findViewById(android.R.id.content),
                            "ASTEROID SELECTED",
                            Snackbar.LENGTH_SHORT // How long to display the message.
                        ).show()
                        viewModel.setTheAsteroid(anAsteroid)
                    })
                    binding.asteroidRecycler.adapter = asteroidAdapter

                }
                Log.d("WWD", "the array size is " + asteroidArray.size)
               // for (asteroid in asteroidArray) {
              //      Log.d("WWD", "asteroid is " + asteroid.toString())
               // }
            }
        })
    }

    private fun fetchImageOfTheDay(binding: FragmentMainBinding) {
        NASAApi.retrofitImageService.getImageOfTheDay(API_KEY).enqueue( object: Callback<ImageOfTheDay> {

            override fun onFailure(call: Call<ImageOfTheDay>, t: Throwable) {
                NASAresponse = "Failure: " + t.message
                Log.d("WWD", "API call failed  " + NASAresponse)
            }

            override fun onResponse(call: Call<ImageOfTheDay>, response: Response<ImageOfTheDay>) {
                val imageObject = response.body() as ImageOfTheDay
                val imageURL: String = imageObject.url
                Log.d("WWD", "got NASA image url " + imageURL)
                Picasso.get().load(imageURL).into(binding.activityMainImageOfTheDay)
            }
        })
    }
}
