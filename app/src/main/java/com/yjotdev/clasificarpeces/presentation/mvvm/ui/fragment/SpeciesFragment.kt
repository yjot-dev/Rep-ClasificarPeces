package com.yjotdev.clasificarpeces.presentation.mvvm.ui.fragment

import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.Lifecycle
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import android.os.Bundle
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import dagger.hilt.android.AndroidEntryPoint
import com.yjotdev.clasificarpeces.presentation.mvvm.viewmodel.UiViewModel
import com.yjotdev.clasificarpeces.presentation.mvvm.ui.adapter.ItemsAdapter
import com.yjotdev.clasificarpeces.presentation.navigation.UiEvent
import com.yjotdev.clasificarpeces.databinding.FragmentSpeciesBinding
import com.yjotdev.clasificarpeces.R

@AndroidEntryPoint
class SpeciesFragment : Fragment() {

    private lateinit var binding: FragmentSpeciesBinding
    private lateinit var adapter: ItemsAdapter
    private val viewModel: UiViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSpeciesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupClickListeners()
        observeViewModelState()
    }

    private fun setupRecyclerView() {
        adapter = ItemsAdapter { item ->
            // Actualiza la información con el item seleccionado
            viewModel.setInfo(item)
            // Navega a la pantalla de información
            findNavController().navigate(R.id.action_species_to_info)
        }
        binding.rvFishList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFishList.adapter = adapter
        // Obtiene la lista de peces completa
        viewModel.getRemoteData()
    }

    private fun setupClickListeners(){
        binding.ibSearch.setOnClickListener {
            val searchedText = binding.etSearch.text.toString()
            viewModel.searchLocalData(searchedText)
        }
        binding.etSearch.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: android.text.Editable?) {
                // Si el texto es vacío entonces muestra toda la lista gracias al patron %% en Room
                val searchedText = p0.toString()
                if(searchedText.isEmpty()){
                    viewModel.searchLocalData(searchedText)
                }
            }
        })
    }

    private fun observeViewModelState(){
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    //Guarda resultados en el adapter
                    adapter.submitList(uiState.fishResult)
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.eventChannel.collect { event ->
                    when (event) {
                        // Muestra un mensaje de exito o error en el Toast
                        is UiEvent.ShowToast -> Toast.makeText(
                            context, event.message, Toast.LENGTH_SHORT
                        ).show()
                        // Muestra el error en el Log
                        is UiEvent.ShowLog -> Log.d("Https",event.message)
                    }
                }
            }
        }
    }
}