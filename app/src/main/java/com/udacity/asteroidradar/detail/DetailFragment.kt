package com.udacity.asteroidradar.detail


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentDetailBinding

class DetailFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentDetailBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val asteroid = DetailFragmentArgs.fromBundle(arguments!!).selectedAsteroid

        binding.asteroid = asteroid

        binding.helpButton.setOnClickListener {
            displayAstronomicalUnitExplanationDialog()
        }

        // set content descriptions
        if (asteroid.isPotentiallyHazardous) {
            binding.activityMainImageOfTheDay.contentDescription = getString(R.string.potentially_hazardous_asteroid_image)
        } else {
            binding.activityMainImageOfTheDay.contentDescription = getString(R.string.not_hazardous_asteroid_image)
        }

        binding.closeApproachDate.text = asteroid.closeApproachDate
        binding.absoluteMagnitude.text = asteroid.absoluteMagnitude.toString()
        binding.distanceFromEarth.text = asteroid.distanceFromEarth.toString()
        binding.estimatedDiameter.text = asteroid.estimatedDiameter.toString()
        binding.relativeVelocity.text  = asteroid.relativeVelocity.toString()

        return binding.root
    }

    private fun displayAstronomicalUnitExplanationDialog() {
        val builder = AlertDialog.Builder(activity!!)
            .setMessage(getString(R.string.astronomica_unit_explanation))
            .setPositiveButton(android.R.string.ok, null)
        builder.create().show()
    }
}
