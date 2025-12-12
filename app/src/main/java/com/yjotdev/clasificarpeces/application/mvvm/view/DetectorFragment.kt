package com.yjotdev.clasificarpeces.application.mvvm.view

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
import kotlin.math.roundToInt
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import com.yjotdev.clasificarpeces.R
import com.yjotdev.clasificarpeces.application.mvvm.viewmodel.UiViewModel
import com.yjotdev.clasificarpeces.application.utils.ImageProcessorHelper
import com.yjotdev.clasificarpeces.databinding.FragmentDetectorBinding

@AndroidEntryPoint
class DetectorFragment : Fragment() {

    @Inject lateinit var imageHelper: ImageProcessorHelper
    private lateinit var binding: FragmentDetectorBinding
    private lateinit var adapter: ItemsAdapter
    private var isDetectionReady = false
    private val viewModel: UiViewModel by activityViewModels()
    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        uri?.let { viewModel.setFishImage(imageHelper.uriToBitmap(uri)) }
    }
    private val takePhoto = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        bitmap?.let { viewModel.setFishImage(bitmap) }
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
        setToolbar()
        setupRecyclerView()
        setupClickListeners()
        observeViewModelState()
    }

    private fun setToolbar(){
        val toolbar = binding.includeToolbar.toolbar
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        toolbar.setupWithNavController(navController, appBarConfiguration)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            // Solo padding arriba para que baje y no choque con la hora
            v.setPadding(0, systemBars.top, 0, 0)
            insets
        }
    }

    private fun setupRecyclerView() {
        adapter = ItemsAdapter {
            if(isDetectionReady) {
                findNavController().navigate(R.id.action_detector_to_info)
            }else{
                Toast.makeText(context, R.string.detectorview_toast_no_detection, Toast.LENGTH_SHORT).show()
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
                isDetectionReady = true
            }?: run {
                Toast.makeText(context, R.string.detectorview_toast_null_photo, Toast.LENGTH_SHORT).show()
                isDetectionReady = false
            }
        }
    }

    private fun observeViewModelState(){
        val labels = requireContext().resources.getStringArray(R.array.infoview_name)
        val descriptions = requireContext().resources.getStringArray(R.array.infoview_description)
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    uiState.fishImage?.let { image ->
                        //Guarda imagen en el imageView
                        binding.imgPhoto.setImageBitmap(image)
                    }
                    uiState.result?.let { result ->
                        //Formato del resultado
                        val items = MutableList(result.size) {""}
                        result.forEach { item ->
                            val percent = (item.score * 100)
                            val i = result.indexOf(item)
                            items[i] = "${labels[i]} - ${percent.roundToInt()}%"
                        }
                        //Guarda datos en el viewModel
                        val i = result.indexOf(result.maxBy { it.score })
                        viewModel.setFishName(labels[i])
                        viewModel.setFishDescription(descriptions[i])
                        //Guarda resultados en el adapter
                        adapter.submitList(items)
                    }
                }
            }
        }
    }
}