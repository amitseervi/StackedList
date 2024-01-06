package com.amit.stackedlist.adapter

import androidx.recyclerview.widget.RecyclerView
import com.amit.stackedlist.databinding.ViewHolderEmiRateOptionBinding
import com.amit.stackedlist.model.ui.EmiRateOptionPresentationItem

class EmiRateOptionViewHolder(
    private val binding: ViewHolderEmiRateOptionBinding,
    private val callback: EmiRateOptionAdapter.ViewHolderCallback
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: EmiRateOptionPresentationItem) {
        binding.radioButton.isChecked = item.selected
        binding.duration.text = item.duration
        binding.title.text = item.amountPerMonth
        binding.specifications.setOnClickListener {
            callback.onClickDetail(item)
        }

        binding.radioButton.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                callback.onSelected(item)
            }
        }
    }

    fun onSelected(selected: Boolean) {
        binding.radioButton.isChecked = selected
    }
}