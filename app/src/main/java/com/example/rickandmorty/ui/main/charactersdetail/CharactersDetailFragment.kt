package com.example.rickandmorty.ui.main.charactersdetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.rickandmorty.R
import com.example.rickandmorty.common.extractIds
import com.example.rickandmorty.common.formatDateString
import com.example.rickandmorty.common.loadImage
import com.example.rickandmorty.databinding.FragmentCharactersDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CharactersDetailFragment : Fragment() {
    private lateinit var binding: FragmentCharactersDetailBinding
    private val viewModel: CharactersDetailViewModel by viewModels()
    private val args: CharactersDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentCharactersDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeCharacter()
        viewModel.isFavourite(args.character.id)
        observeFavoriteStatus()

        binding.apply {
            ivBack.setOnClickListener { goBack() }
            ivFav.setOnClickListener {
                viewModel.isFavourite.value?.let { isFavorite ->
                    if (!isFavorite) {
                        viewModel.insertFavCharacter(args.character)
                    }
                }
            }
        }
    }

    private fun observeCharacter() {
        viewModel.setCharacter(args.character)
        viewModel.character.observe(viewLifecycleOwner) { character ->
            binding.tvCharacterDetailName.text = character.name
            binding.tvStatusDetail.text = character.status
            binding.tvSpecyDetail.text = character.species
            binding.tvGenderDetail.text = character.gender
            binding.tvOriginDetail.text = character.origin.name
            binding.tvLocationDetail.text = character.location.name
            binding.tvEpisodesDetail.text = character.episode.extractIds()
            binding.tvCreatedDetail.text = character.created.formatDateString()
            binding.ivCharacterDetail.loadImage(character.image)
        }
    }

    private fun goBack() {
        findNavController().navigateUp()
    }

    private fun observeFavoriteStatus() {
        viewModel.isFavourite.observe(viewLifecycleOwner) { isFavorite ->
            binding.ivFav.setImageResource(
                if (isFavorite) R.drawable.iv_fav else R.drawable.iv_normal
            )
        }
    }
}
