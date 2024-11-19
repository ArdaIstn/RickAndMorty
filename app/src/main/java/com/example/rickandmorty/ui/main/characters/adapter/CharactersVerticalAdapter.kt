package com.example.rickandmorty.ui.main.characters.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmorty.R
import com.example.rickandmorty.common.loadImage
import com.example.rickandmorty.data.model.Character
import com.example.rickandmorty.databinding.RvColumItemBinding

class CharactersVerticalAdapter(
    private val characters: List<Character>,
    private val onCharacterSelected: (Character) -> Unit,

    ) : RecyclerView.Adapter<CharactersVerticalAdapter.CharacterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val binding = RvColumItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CharacterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        holder.bind(characters[position])
    }

    override fun getItemCount() = characters.size

    inner class CharacterViewHolder(private val binding: RvColumItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(character: Character) {
            binding.tvCharacterName.text = character.name
            binding.ivCharacter.loadImage(character.image)
            binding.tvDeadOrAlive.text = character.status

            when (character.status) {
                "Alive" -> binding.ivDeadOrAlive.setBackgroundResource(R.drawable.ic_alive)
                "Dead" -> binding.ivDeadOrAlive.setBackgroundResource(R.drawable.ic_dead)
                "unknown" -> binding.ivDeadOrAlive.setBackgroundResource(R.drawable.ic_unknown)
            }


            binding.root.setOnClickListener { onCharacterSelected(character) }

        }
    }
}
