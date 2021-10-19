package com.udacity.asteroidradar

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.databinding.ListItemAsteroidBinding

class AsteroidAdapter(val asteroidList: List<Asteroid>, val clickListener: AsteroidClickListener): RecyclerView.Adapter<AsteroidAdapter.AsteroidViewHolder>() {
//class AsteroidAdapter(val asteroidList: List<Asteroid> ): RecyclerView.Adapter<AsteroidAdapter.AsteroidViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidViewHolder {
       // Log.d("WWD", "In AsteroidAdapter onCreateViewHolder")
        val layoutInflater = LayoutInflater.from(parent.context)
       // val view = layoutInflater.inflate(R.layout.list_item_asteroid, parent, false)
        val binding = ListItemAsteroidBinding.inflate(layoutInflater, parent, false)
        return AsteroidViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AsteroidViewHolder, position: Int) {
        val anAsteroid = asteroidList[position]
        //Log.d("WWD", " -------------------in onBindViewHolder position is " + position)
       // Log.d("WWD", "--------------------name is " + anAsteroid.codename)
       // Log.d("WWD", "--------------------date is " + anAsteroid.closeApproachDate)
        holder.binding.asteroidName.text = anAsteroid.codename
        holder.binding.asteroidDate.text = anAsteroid.closeApproachDate
        holder.binding.asteroid = anAsteroid
        holder.binding.clickListener = clickListener
        holder.binding.executePendingBindings()

    }

    override fun getItemCount(): Int {
        Log.d("WWD", "in getItemCount count is " + asteroidList.size)
        return asteroidList.size
    }

    class AsteroidViewHolder(val binding: ListItemAsteroidBinding) : RecyclerView.ViewHolder(binding.root) {
        val asteroidName: TextView = itemView.findViewById(R.id.asteroid_name)
        val asteroidDate: TextView = itemView.findViewById(R.id.asteroid_date)
    }
}

class AsteroidClickListener(val clickListener: (selectedAsteroid: Asteroid) -> Unit) {
    fun onClick(selectedAsteroid: Asteroid) = clickListener(selectedAsteroid)
}


