package com.example.rickandmorty.ui.main.characters

import CharactersVerticalAdapter
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmorty.databinding.FragmentCharactersBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CharactersFragment : Fragment() {
    private lateinit var binding: FragmentCharactersBinding
    private val viewModel: CharactersViewModel by viewModels()

    private var horizontalScrollState: Parcelable? = null // Yatay RecyclerView scroll durumu
    private var verticalScrollState: Parcelable? = null // Dikey RecyclerView scroll durumu
    private var selectedLocationPosition: Int = RecyclerView.NO_POSITION // Seçili lokasyon
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

        // Lokasyonları gözlemle
        observeLocations()

        // Karakterleri gözlemle
        observeCharacters()


    }

    override fun onPause() {
        super.onPause()
        // Scroll durumlarını ve seçili öğeyi kaydet
        horizontalScrollState = binding.horizontalRv.layoutManager?.onSaveInstanceState()
        verticalScrollState = binding.verticalRv.layoutManager?.onSaveInstanceState()
        selectedLocationPosition = locationsAdapter?.selectedPosition ?: RecyclerView.NO_POSITION
    }

    private fun observeLocations() {
        viewModel.locations.observe(viewLifecycleOwner) { locations ->
            locationsAdapter = LocationsHorizontalAdapter(locations) { location ->
                viewModel.fetchCharactersByLocation(location.residents)
            }.apply {
                // Adapter'ın seçili pozisyonunu kaydedilen duruma göre ayarla
                selectedPosition = selectedLocationPosition

            }

            binding.horizontalRv.apply {
                adapter = locationsAdapter
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

                // Scroll durumunu geri yükle
                horizontalScrollState?.let { state ->
                    layoutManager?.onRestoreInstanceState(state)
                }
            }
        }
    }

    private fun observeCharacters() {
        // Karakter listesini gözlemle
        viewModel.characters.observe(viewLifecycleOwner) { characters ->
            charactersAdapter = CharactersVerticalAdapter(characters) { character ->
                val action = CharactersFragmentDirections.actionCharactersToDetail(character)
                findNavController().navigate(action)
            }

            binding.verticalRv.apply {
                adapter = charactersAdapter
                layoutManager = LinearLayoutManager(requireContext())

                // Scroll durumunu geri yükle
                verticalScrollState?.let { state ->
                    layoutManager?.onRestoreInstanceState(state)
                }
            }
        }
    }
}
