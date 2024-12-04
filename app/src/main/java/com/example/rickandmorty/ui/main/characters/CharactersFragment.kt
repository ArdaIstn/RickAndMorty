package com.example.rickandmorty.ui.main.characters

import com.example.rickandmorty.ui.main.characters.adapter.CharactersVerticalAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView.OnQueryTextListener
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rickandmorty.common.gone
import com.example.rickandmorty.common.navigateTo
import com.example.rickandmorty.common.visible
import com.example.rickandmorty.data.model.Character
import com.example.rickandmorty.databinding.FragmentCharactersBinding
import com.example.rickandmorty.ui.main.characters.adapter.LocationsHorizontalAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CharactersFragment : Fragment() {

    private lateinit var binding: FragmentCharactersBinding
    private val viewModel: CharactersViewModel by viewModels()

    private val locationsAdapter: LocationsHorizontalAdapter by lazy {
        LocationsHorizontalAdapter { location ->
            viewModel.fetchCharactersByLocation(location.residents)
            binding.searchView.clearFocus()
            binding.searchView.setQuery("", false)
        }
    }

    private val charactersAdapter: CharactersVerticalAdapter by lazy {
        CharactersVerticalAdapter { character ->
            navigateToCharacterDetail(character)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentCharactersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerViews()
        setupObservers()
        setupSearchView()
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Submit işlemi yapılmazsa true döner
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // ViewModel'e arama sorgusunu gönderiyoruz
                newText?.let {
                    viewModel.filterCharacters(it)
                    if (it.isEmpty()) {
                        binding.verticalRv.scrollToPosition(0)
                    }
                }
                return true
            }
        })
    }


    private fun setupRecyclerViews() {
        binding.horizontalRv.apply {
            adapter = locationsAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
        binding.verticalRv.apply {
            adapter = charactersAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }


    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                handleErrorState(state)
                handleLocationsState(state)
                handleCharactersState(state)
                handleEmptyState(state)

                if (locationsAdapter.selectedPosition == -1) {
                    val firstLocationWithResidents = state.locations?.firstOrNull { location ->
                        location.residents.isNotEmpty()
                    }

                    firstLocationWithResidents?.let {
                        locationsAdapter.selectedPosition =
                            0 // İlk lokasyonu seçili olarak işaretle
                        viewModel.fetchCharactersByLocation(it.residents) // İlk lokasyondaki karakterleri yükle
                    }
                }

            }
        }
    }


    private fun handleEmptyState(state: CharactersViewModel.CharacterState) {
        if (state.isEmpty) {
            binding.animationView.visible()
        }
    }

    private fun handleErrorState(state: CharactersViewModel.CharacterState) {
        state.failMessage?.let {
            Toast.makeText(requireContext(), "Data Error!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleLocationsState(state: CharactersViewModel.CharacterState) {
        state.locations?.let { locations ->
            locationsAdapter.differ.submitList(locations)
        }
    }

    private fun handleCharactersState(state: CharactersViewModel.CharacterState) {
        state.characters?.let { characters ->
            charactersAdapter.differ.submitList(characters)
            if (characters.isEmpty()) {
                binding.animationView.visible()
            } else {
                binding.animationView.gone()
            }

        }
    }

    private fun navigateToCharacterDetail(character: Character) {
        val action = CharactersFragmentDirections.actionCharactersToDetail(character)
        navigateTo(action)
        binding.searchView.clearFocus()
    }
}


