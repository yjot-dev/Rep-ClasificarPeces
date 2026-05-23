package com.yjotdev.clasificarpeces.presentation.mvvm.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yjotdev.clasificarpeces.databinding.ItemsBinding

class ItemsAdapter (
    private val onAnyItemClicked: () -> Unit
) : ListAdapter<String, ItemsAdapter.ViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    class ViewHolder(val binding: ItemsBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Asignamos el texto de la lista al TextView
        holder.binding.txtResult.text = getItem(position)
        // Evento de click en un item
        holder.itemView.setOnClickListener {
            onAnyItemClicked()
        }
    }
}