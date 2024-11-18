package com.example.rickandmorty.ui.main.charactersdetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.rickandmorty.R
import com.example.rickandmorty.common.extractIds
import com.example.rickandmorty.common.formatDateString
import com.example.rickandmorty.common.loadImage
import com.example.rickandmorty.data.model.Character
import com.example.rickandmorty.databinding.FragmentCharactersDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CharactersDetailFragment : Fragment() {
    private lateinit var binding: FragmentCharactersDetailBinding
    private val viewModel: CharactersDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentCharactersDetailBinding.inflate(inflater, container, false)
        setArguments()

        viewModel.character.observe(viewLifecycleOwner) { character ->
            binding.tvCharacterDetailName.text = character.name
            binding.tvStatusDetail.text = character.status
            binding.tvSpecyDetail.text = character.species
            binding.tvGenderDetail.text = character.gender
            binding.tvOriginDetail.text = character.origin.name
            binding.tvLocationDetail.text = character.location.name
            binding.tvEpiosedDetail.text = character.episode.extractIds()
            binding.tvCreatedDetail.text = character.created.formatDateString()
            binding.ivCharacterDetail.loadImage(character.image)

        }


        return binding.root
    }

    private fun setArguments() {
        arguments?.let {
            val args = CharactersDetailFragmentArgs.fromBundle(it).character
            viewModel.setCharacter(args)
        }
    }


}

