package com.yjotdev.clasificarpeces.presentation.mvvm.ui.fragment

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import com.yjotdev.clasificarpeces.R
import com.yjotdev.clasificarpeces.databinding.FragmentStartBinding

@AndroidEntryPoint
class StartFragment : Fragment() {

    private lateinit var binding: FragmentStartBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
    }

    private fun setupClickListeners(){
        binding.btnNext.setOnClickListener {
            findNavController().navigate(R.id.action_start_to_detector)
        }
    }
}