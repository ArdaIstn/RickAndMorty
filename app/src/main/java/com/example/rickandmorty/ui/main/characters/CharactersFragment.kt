package com.example.rickandmorty.ui.main.characters

import com.example.rickandmorty.ui.main.characters.adapter.CharactersVerticalAdapter
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView.OnQueryTextListener
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rickandmorty.R
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

    private var _binding: FragmentCharactersBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CharactersViewModel by viewModels()
    private var verticalRecyclerViewState: Parcelable? = null

    private val locationsAdapter by lazy {
        LocationsHorizontalAdapter { location ->
            viewModel.getCharacter(location.residents)
            clearSearchView()
        }
    }

    private val charactersAdapter by lazy {
        CharactersVerticalAdapter { character ->
            navigateToCharacterDetail(character)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        Log.e("CharactersFragment", "onCreateView called")
        _binding = FragmentCharactersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.e("CharactersFragment", "onViewCreated called")
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupObservers()
    }

    private fun setupUI() {
        setupRecyclerViews()
        setupSearchView()
        setupStatusMenuListener()
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

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    viewModel.filterCharacters(it)
                    binding.verticalRv.scrollToPosition(0)
                }
                return true
            }
        })
    }

    private fun setupStatusMenuListener() {
        binding.toolbarCharacters.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.filterDead -> viewModel.filterCharactersByStatus("Dead")
                R.id.filterAlive -> viewModel.filterCharactersByStatus("Alive")
                R.id.filterUnknown -> viewModel.filterCharactersByStatus("unknown")
                R.id.removeFilter -> viewModel.filterCharactersByStatus("")
                else -> return@setOnMenuItemClickListener false
            }
            true
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                handleUIState(state)
            }
        }
    }

    private fun handleUIState(state: CharactersViewModel.CharacterState) {
        handleLoadingState(state)
        handleErrorState(state)
        handleLocationsState(state)
        handleCharactersState(state)
        handleEmptyState(state)
        handleInitialSelection(state)
    }

    private fun handleLoadingState(state: CharactersViewModel.CharacterState) {
        if (state.isLoading) binding.progressBar.visible() else binding.progressBar.gone()
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
            if (characters.isEmpty()) binding.animationView.visible() else binding.animationView.gone()
        }
    }

    private fun handleEmptyState(state: CharactersViewModel.CharacterState) {
        if (state.isEmpty) binding.animationView.visible() else binding.animationView.gone()
    }

    private fun handleInitialSelection(state: CharactersViewModel.CharacterState) {
        if (locationsAdapter.selectedPosition == -1) {
            state.locations?.firstOrNull { it.residents.isNotEmpty() }?.let {
                locationsAdapter.selectedPosition = 0
                viewModel.getCharacter(it.residents)
            }
        }
    }

    private fun navigateToCharacterDetail(character: Character) {
        val action = CharactersFragmentDirections.actionCharactersToDetail(character)
        navigateTo(action)
    }

    private fun clearSearchView() {
        binding.searchView.apply {
            setQuery("", false)
            clearFocus()
        }
    }

    fun resetVerticalRecyclerView() {
        binding.verticalRv.smoothScrollToPosition(0)
    }

    override fun onPause() {
        super.onPause()
        verticalRecyclerViewState = binding.verticalRv.layoutManager?.onSaveInstanceState()
        clearSearchView()
    }

    override fun onResume() {
        super.onResume()
        binding.verticalRv.layoutManager?.onRestoreInstanceState(verticalRecyclerViewState)
    }


}

