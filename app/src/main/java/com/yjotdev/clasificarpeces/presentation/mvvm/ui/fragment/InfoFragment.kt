package com.yjotdev.clasificarpeces.presentation.mvvm.ui.fragment

import androidx.fragment.app.activityViewModels
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.coroutines.launch
import kotlin.getValue
import dagger.hilt.android.AndroidEntryPoint
import com.yjotdev.clasificarpeces.presentation.mvvm.viewmodel.UiViewModel
import com.yjotdev.clasificarpeces.databinding.FragmentInfoBinding

@AndroidEntryPoint
class InfoFragment : Fragment() {

    private lateinit var binding: FragmentInfoBinding
    private val viewModel: UiViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModelState()
    }

    private fun observeViewModelState(){
        binding.txtDescription.movementMethod = ScrollingMovementMethod()
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    binding.imgPhoto.setImageBitmap(uiState.fishImage)
                    binding.txtName.text = uiState.fishInfo.name
                    binding.txtDescription.text = uiState.fishInfo.description
                }
            }
        }
    }
}