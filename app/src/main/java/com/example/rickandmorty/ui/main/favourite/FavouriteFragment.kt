package com.example.rickandmorty.ui.main.favourite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmorty.R
import com.example.rickandmorty.data.model.Character
import com.example.rickandmorty.data.model.CharacterFav
import com.example.rickandmorty.databinding.FragmentFavouriteBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class FavouriteFragment : Fragment() {
    private lateinit var binding: FragmentFavouriteBinding
    private val viewModel: FavouriteViewModel by viewModels()
    private val favouriteAdapter by lazy {
        FavouriteAdapter(onFavCharacterSelected = {
            showDeleteConfirmationDialog(it.id)
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

        binding.toolbarFavourite.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.deleteAll -> {
                    clickDeleteAll()
                    true
                }

                else -> {
                    false
                }
            }
        }
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
                if (it.isEmpty()) binding.favAnimationView.visibility =
                    View.VISIBLE else binding.favAnimationView.visibility = View.GONE
            }
        }
    }

    private fun showDeleteConfirmationDialog(characterId: Int) {
        MaterialAlertDialogBuilder(requireContext()).setTitle("Delete?")
            .setMessage("Are you sure you want to delete this character from your favorites?")
            .setPositiveButton("Yes") { dialog, _ ->
                viewModel.deleteCharacterById(characterId)
                Snackbar.make(requireView(), "Character deleted.", Snackbar.LENGTH_SHORT).show()
                dialog.dismiss()
            }.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    private fun clickDeleteAll() {
        viewModel.deleteAll()
        Snackbar.make(requireView(), "All characters deleted", Snackbar.LENGTH_SHORT).show()
    }

}
