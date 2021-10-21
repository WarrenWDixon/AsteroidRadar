package com.udacity.asteroidradar

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.databinding.ListItemAsteroidBinding

class AsteroidAdapter( val clickListener: AsteroidClickListener): RecyclerView.Adapter<AsteroidAdapter.AsteroidViewHolder>() {
    var asteroidList: List<Asteroid> = emptyList()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListItemAsteroidBinding.inflate(layoutInflater, parent, false)
        return AsteroidViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AsteroidViewHolder, position: Int) {
        val anAsteroid = asteroidList[position]
        holder.binding.asteroidName.text = anAsteroid.codename
        holder.binding.asteroidName.contentDescription = anAsteroid.codename
        holder.binding.asteroidDate.text = anAsteroid.closeApproachDate
        holder.binding.asteroidDate.contentDescription = anAsteroid.closeApproachDate

        holder.binding.asteroid = anAsteroid
        holder.binding.clickListener = clickListener
        holder.binding.executePendingBindings()

    }

    override fun getItemCount(): Int {
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


