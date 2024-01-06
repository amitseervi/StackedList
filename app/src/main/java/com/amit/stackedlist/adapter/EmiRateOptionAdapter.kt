package com.amit.stackedlist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.amit.stackedlist.databinding.ViewHolderEmiRateOptionBinding
import com.amit.stackedlist.model.ui.EmiRateOptionPresentationItem

class EmiRateOptionAdapter(private val callback: ViewHolderCallback) :
    ListAdapter<EmiRateOptionPresentationItem, EmiRateOptionViewHolder>(
        object : DiffUtil.ItemCallback<EmiRateOptionPresentationItem>() {
            override fun areItemsTheSame(
                oldItem: EmiRateOptionPresentationItem,
                newItem: EmiRateOptionPresentationItem
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: EmiRateOptionPresentationItem,
                newItem: EmiRateOptionPresentationItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun getChangePayload(
                oldItem: EmiRateOptionPresentationItem,
                newItem: EmiRateOptionPresentationItem
            ): Any? {
                return if (oldItem.id == newItem.id &&
                    oldItem.amountPerMonth == newItem.amountPerMonth &&
                    oldItem.duration == newItem.duration
                ) {
                    EmiRateOptionSelectPayload(newItem.selected)
                } else {
                    null
                }
            }

        }
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmiRateOptionViewHolder {
        val binding = ViewHolderEmiRateOptionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return EmiRateOptionViewHolder(binding, callback)
    }

    override fun onBindViewHolder(holder: EmiRateOptionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onBindViewHolder(
        holder: EmiRateOptionViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        val rateChangePayload =
            payloads.filterIsInstance<EmiRateOptionSelectPayload>().firstOrNull()
        if (rateChangePayload != null) {
            holder.onSelected(rateChangePayload.checked)
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    interface ViewHolderCallback {
        fun onSelected(selectedOption: EmiRateOptionPresentationItem)
        fun onClickDetail(selectedOption: EmiRateOptionPresentationItem)
    }
}

