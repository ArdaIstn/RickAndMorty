package com.example.rickandmorty.ui.main.characters

import com.example.rickandmorty.ui.main.characters.adapter.CharactersVerticalAdapter
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmorty.common.navigateTo
import com.example.rickandmorty.data.model.Character
import com.example.rickandmorty.data.model.Result
import com.example.rickandmorty.databinding.FragmentCharactersBinding
import com.example.rickandmorty.ui.main.characters.adapter.LocationsHorizontalAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CharactersFragment : Fragment() {
    private lateinit var binding: FragmentCharactersBinding
    private val viewModel: CharactersViewModel by viewModels()

    private var horizontalScrollState: Parcelable? = null
    private var verticalScrollState: Parcelable? = null
    private var selectedLocationPosition: Int = RecyclerView.NO_POSITION
    private var locationsAdapter: LocationsHorizontalAdapter? = null
    private var charactersAdapter: CharactersVerticalAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentCharactersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
    }

    override fun onPause() {
        super.onPause()
        saveRecyclerViewStates()
    }

    private fun setupObservers() {
        observeLocations()
        observeCharacters()

        viewModel.isEmpty.observe(viewLifecycleOwner) { isEmpty ->
            binding.isEmpty.visibility = if (isEmpty) View.VISIBLE else View.GONE
        }
    }

    private fun observeLocations() {
        viewModel.locations.observe(viewLifecycleOwner) { locations ->
            setupLocationsAdapter(locations)

            if (selectedLocationPosition == RecyclerView.NO_POSITION && locations.isNotEmpty()) {
                selectFirstLocation(locations)
            }
        }
    }

    private fun observeCharacters() {
        viewModel.characters.observe(viewLifecycleOwner) { characters ->
            setupCharactersAdapter(characters)
        }
    }

    private fun setupLocationsAdapter(locations: List<Result>) {
        locationsAdapter = LocationsHorizontalAdapter(locations) { location ->
            resetVerticalScrollState() // Yeni lokasyonda listeyi sıfırla
            viewModel.fetchCharactersByLocation(location.residents)
        }.apply {
            selectedPosition =
                if (selectedLocationPosition == RecyclerView.NO_POSITION) 0 else selectedLocationPosition
        }

        binding.horizontalRv.apply {
            adapter = locationsAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            restoreHorizontalScrollState()
        }
    }

    private fun setupCharactersAdapter(characters: List<Character>) {
        charactersAdapter = CharactersVerticalAdapter(characters) { character ->
            navigateToCharacterDetail(character)
        }

        binding.verticalRv.apply {
            adapter = charactersAdapter
            layoutManager = LinearLayoutManager(requireContext())
            scrollToTopOrRestoreState()
        }
    }

    private fun selectFirstLocation(locations: List<Result>) {
        selectedLocationPosition = 0
        locationsAdapter?.notifyItemChanged(selectedLocationPosition)
        viewModel.fetchCharactersByLocation(locations[0].residents)
    }

    private fun saveRecyclerViewStates() {
        horizontalScrollState = binding.horizontalRv.layoutManager?.onSaveInstanceState()
        verticalScrollState = binding.verticalRv.layoutManager?.onSaveInstanceState()
        selectedLocationPosition = locationsAdapter?.selectedPosition ?: RecyclerView.NO_POSITION
    }

    private fun restoreHorizontalScrollState() {
        horizontalScrollState?.let { state ->
            binding.horizontalRv.layoutManager?.onRestoreInstanceState(state)
        }
    }

    private fun scrollToTopOrRestoreState() {
        if (verticalScrollState == null) {
            binding.verticalRv.scrollToPosition(0)
        } else {
            binding.verticalRv.layoutManager?.onRestoreInstanceState(verticalScrollState)
        }
    }

    private fun resetVerticalScrollState() {
        verticalScrollState = null // Lokasyon değişiminde scroll durumunu sıfırla
    }

    private fun navigateToCharacterDetail(character: Character) {
        val action = CharactersFragmentDirections.actionCharactersToDetail(character)
        navigateTo(action)
    }
}
