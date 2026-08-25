package com.yjotdev.clasificarpeces.presentation.mvvm.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yjotdev.clasificarpeces.databinding.AdapterSpeciesBinding
import com.yjotdev.clasificarpeces.domain.model.SpeciesModel

class ItemsAdapter (
    private val onAnyItemClicked: (SpeciesModel) -> Unit
) : ListAdapter<SpeciesModel, ItemsAdapter.ViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<SpeciesModel>() {
        override fun areItemsTheSame(oldItem: SpeciesModel, newItem: SpeciesModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: SpeciesModel, newItem: SpeciesModel): Boolean {
            return oldItem == newItem
        }
    }

    class ViewHolder(val binding: AdapterSpeciesBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterSpeciesBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Asignamos los datos a los TextView del item
        val item = getItem(position)
        holder.binding.tvCommonName.text = item.commonName
        holder.binding.tvScientificName.text = item.scientificName
        // Evento de clic en un item
        holder.itemView.setOnClickListener {
            onAnyItemClicked(item)
        }
    }
}