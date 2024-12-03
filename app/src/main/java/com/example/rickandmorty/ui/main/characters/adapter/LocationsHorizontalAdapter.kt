package com.example.rickandmorty.ui.main.characters.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmorty.R
import com.example.rickandmorty.data.model.Result
import com.example.rickandmorty.databinding.RvRowItemBinding

class LocationsHorizontalAdapter(
    private val onLocationSelected: (Result) -> Unit
) : RecyclerView.Adapter<LocationsHorizontalAdapter.LocationViewHolder>() {

    // Seçilen pozisyonu takip etmek için bir değişken ekliyoruz
     var selectedPosition: Int = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RvRowItemBinding.inflate(inflater, parent, false)
        return LocationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        val location = differ.currentList[position]
        holder.bind(location, position)
    }

    override fun getItemCount() = differ.currentList.size


    inner class LocationViewHolder(private val binding: RvRowItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(result: Result, position: Int) {
            binding.tvLocation.text = "${result.name}(${result.residents.size})"

            if (selectedPosition == position) {
                binding.locationCard.setBackgroundResource(R.drawable.card_selected)
            } else {
                binding.locationCard.setBackgroundResource(R.drawable.card_unselected)
            }

            // Tıklama olayı
            binding.root.setOnClickListener {
                // Seçili pozisyonu güncelle
                val previousPosition = selectedPosition
                selectedPosition = position

                // Eski ve yeni pozisyonları güncelle
                notifyItemChanged(previousPosition)
                notifyItemChanged(selectedPosition)

                onLocationSelected(result)
            }
        }
    }

    private val diffCallBack = object : DiffUtil.ItemCallback<Result>() {
        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem == newItem

        }

    }
    val differ = AsyncListDiffer(this, diffCallBack)

}



