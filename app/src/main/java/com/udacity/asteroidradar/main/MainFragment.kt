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
//import com.udacity.asteroidradar.api.getNASAAsteroids
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
    private var asteroidArray = mutableListOf<Asteroid>()
    private lateinit var binding: FragmentMainBinding
    var NASAresponse = ""

    private val viewModel: MainViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onViewCreated()"
        }
        ViewModelProvider(this, MainViewModel.Factory(activity.application)).get(MainViewModel::class.java)

    }

    private lateinit var  binding2 : FragmentMainBinding
    private var asteroidAdapter: AsteroidAdapter? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        setHasOptionsMenu(true)

        asteroidAdapter = AsteroidAdapter(AsteroidClickListener { anAsteroid ->
            viewModel.setTheAsteroid(anAsteroid)
        })
        binding.asteroidRecycler.adapter = asteroidAdapter
        fetchImageOfTheDay(binding)
        viewModel.theAsteroid.observe(viewLifecycleOwner) { selectedAsteroid ->
            if (null != selectedAsteroid) {
                this.findNavController()
                    .navigate(MainFragmentDirections.actionShowDetail(selectedAsteroid))
                viewModel.displayAsteroidDetailCompleted()
            }
        }

        viewModel.asteroids.observe(viewLifecycleOwner) {
            asteroidAdapter?.asteroidList = it
        }
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }


    private fun fetchImageOfTheDay(binding: FragmentMainBinding) {
        NASAApi.retrofitImageService.getImageOfTheDay(Constants.API_KEY).enqueue( object: Callback<ImageOfTheDay> {

            override fun onFailure(call: Call<ImageOfTheDay>, t: Throwable) {
                Log.d("WWD", "API call failed  " + t.message)
            }

            override fun onResponse(call: Call<ImageOfTheDay>, response: Response<ImageOfTheDay>) {
                val imageObject = response.body() as ImageOfTheDay
                val imageURL: String = imageObject.url
                binding.activityMainImageOfTheDay.contentDescription = imageObject.title
                Picasso.get().load(imageURL).into(binding.activityMainImageOfTheDay)
            }
        })
    }
}
