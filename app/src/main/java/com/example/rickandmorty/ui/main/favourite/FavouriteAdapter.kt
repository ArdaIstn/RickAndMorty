package com.example.rickandmorty.ui.main.favourite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmorty.R
import com.example.rickandmorty.common.loadImage
import com.example.rickandmorty.data.model.CharacterFav
import com.example.rickandmorty.databinding.RvFavItemBinding

class FavouriteAdapter(private val onFavCharacterSelected: (CharacterFav) -> Unit) :
    RecyclerView.Adapter<FavouriteAdapter.FavouriteViewHolder>() {

    inner class FavouriteViewHolder(private val binding: RvFavItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(character: CharacterFav) {
            binding.tvFavCharacterName.text = character.name
            character.image?.let { binding.ivFavCharacter.loadImage(it) }
            binding.tvFavDeadOrAlive.text = character.status
            binding.tvFavCardSpecy.text = character.species

            when (character.status) {
                "Alive" -> binding.ivFavDeadOrAlive.setBackgroundResource(R.drawable.ic_alive)
                "Dead" -> binding.ivFavDeadOrAlive.setBackgroundResource(R.drawable.ic_dead)
                "unknown" -> binding.ivFavDeadOrAlive.setBackgroundResource(R.drawable.ic_unknown)
            }


            binding.root.setOnClickListener {
                onFavCharacterSelected(character)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): FavouriteViewHolder {
        val binding = RvFavItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavouriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private val diffCallBack = object : DiffUtil.ItemCallback<CharacterFav>() {

        override fun areItemsTheSame(oldItem: CharacterFav, newItem: CharacterFav): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CharacterFav, newItem: CharacterFav): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, diffCallBack)


}
