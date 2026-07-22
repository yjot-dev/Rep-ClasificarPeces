package com.yjotdev.clasificarpeces.presentation.mvvm.ui.fragment

import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.Lifecycle
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import dagger.hilt.android.AndroidEntryPoint
import com.yjotdev.clasificarpeces.presentation.mvvm.viewmodel.UiViewModel
import com.yjotdev.clasificarpeces.presentation.mvvm.ui.adapter.ItemsAdapter
import com.yjotdev.clasificarpeces.presentation.utils.ImageInput
import com.yjotdev.clasificarpeces.presentation.navigation.UiEvent
import com.yjotdev.clasificarpeces.databinding.FragmentDetectorBinding
import com.yjotdev.clasificarpeces.R

@AndroidEntryPoint
class DetectorFragment : Fragment() {

    private lateinit var binding: FragmentDetectorBinding
    private lateinit var adapter: ItemsAdapter
    private var isClickableList = false
    private val viewModel: UiViewModel by activityViewModels()
    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        uri?.let { viewModel.setFishImage(ImageInput.FromUri(uri)) }
    }
    private val takePhoto = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        bitmap?.let { viewModel.setFishImage(ImageInput.FromBitmap(bitmap)) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetectorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupClickListeners()
        observeViewModelState()
    }

    private fun setupRecyclerView() {
        adapter = ItemsAdapter {
            if (isClickableList) {
                findNavController().navigate(R.id.action_detector_to_info)
            } else {
                Toast.makeText(
                    context,
                    R.string.detectorview_toast_no_detection,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        binding.listDetector.layoutManager = LinearLayoutManager(requireContext())
        binding.listDetector.adapter = adapter
    }

    private fun setupClickListeners(){
        binding.btnChoosePhoto.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        binding.btnTakePhoto.setOnClickListener {
            takePhoto.launch(null)
        }
        binding.btnDetector.setOnClickListener {
            val currentImage = viewModel.uiState.value.fishImage
            currentImage?.let { bitmap ->
                viewModel.classifierResult(bitmap)
            }?: run {
                Toast.makeText(context, R.string.detectorview_toast_null_photo, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeViewModelState(){
        val labels = resources.getStringArray(R.array.infoview_name)
        val descriptions = resources.getStringArray(R.array.infoview_description)
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    uiState.fishImage?.let { image ->
                        //Guarda imagen en el imageView
                        binding.imgPhoto.setImageBitmap(image)
                    }
                    uiState.fishResult.let {
                        //Guarda datos en el viewModel
                        viewModel.showInfo(labels, descriptions)
                        //Guarda resultados en el adapter
                        adapter.submitList(viewModel.getTranslatedList(labels))
                    }
                    //La lista se hace clicable
                    isClickableList = !uiState.fishResult.isNullOrEmpty()
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