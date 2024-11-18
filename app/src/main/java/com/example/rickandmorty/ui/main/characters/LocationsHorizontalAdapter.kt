package com.example.rickandmorty.ui.main.characters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmorty.R
import com.example.rickandmorty.data.model.Result
import com.example.rickandmorty.databinding.RvRowItemBinding

class LocationsHorizontalAdapter(
    private val results: List<Result>, private val onLocationSelected: (Result) -> Unit
) : RecyclerView.Adapter<LocationsHorizontalAdapter.LocationViewHolder>() {

    var selectedPosition: Int = RecyclerView.NO_POSITION // Seçili öğeyi takip etmek için

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RvRowItemBinding.inflate(inflater, parent, false)
        return LocationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        val location = results[position]
        holder.bind(location, position)
    }

    override fun getItemCount() = results.size

    inner class LocationViewHolder(private val binding: RvRowItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(result: Result, position: Int) {
            binding.tvLocation.text = result.name

            // Seçili öğe için siyah, diğerleri için beyaz arka plan
            if (position == selectedPosition) {
                binding.locationCard.setBackgroundResource(R.color.black)
            } else {
                binding.locationCard.setBackgroundResource(R.color.white)
            }

            binding.root.setOnClickListener {
                // Yeni öğeye tıklanınca önce eski seçimi kaldır
                val previousPosition = selectedPosition
                selectedPosition = position

                // Eski ve yeni pozisyonları güncelle
                notifyItemChanged(previousPosition)
                notifyItemChanged(selectedPosition)

                // Callback çağır
                onLocationSelected(result)
            }
        }
    }
}
