package com.amit.stackedlist.adapter

import androidx.recyclerview.widget.RecyclerView
import com.amit.stackedlist.databinding.ViewHolderBankAccountSelectBinding
import com.amit.stackedlist.model.ui.UserBankAccountItemPresentation

class BankOptionViewHolder(
    private val binding: ViewHolderBankAccountSelectBinding,
    private val callback: BankAccountOptionAdapter.ViewHolderCallback
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: UserBankAccountItemPresentation) {
        binding.radioButton.isChecked = item.selected
        binding.bankAccount.text = item.accountName
        binding.bankName.text = item.bankName

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