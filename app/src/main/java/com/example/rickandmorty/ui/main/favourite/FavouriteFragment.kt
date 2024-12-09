package com.example.rickandmorty.ui.main.favourite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rickandmorty.R
import com.example.rickandmorty.databinding.FragmentFavouriteBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class FavouriteFragment : Fragment() {
    private lateinit var binding: FragmentFavouriteBinding
    private val viewModel: FavouriteViewModel by viewModels()
    private val favouriteAdapter by lazy {
        FavouriteAdapter(onFavCharacterSelected = {
            viewModel.deleteCharacterById(it.id)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        observeFavouriteCharacters()

    }


    private fun setUpRecyclerView() {
        binding.favRv.apply {
            adapter = favouriteAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun observeFavouriteCharacters() {
        lifecycleScope.launch {
            viewModel.favCharacters.observe(viewLifecycleOwner) {
                favouriteAdapter.differ.submitList(it)
                if (it.isEmpty()) {
                    Snackbar.make(requireView(), "Liste boş", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }
}