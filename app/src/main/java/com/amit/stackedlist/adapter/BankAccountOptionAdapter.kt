package com.amit.stackedlist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.amit.stackedlist.databinding.ViewHolderBankAccountSelectBinding
import com.amit.stackedlist.model.ui.UserBankAccountItemPresentation

class BankAccountOptionAdapter(private val callback: ViewHolderCallback) :
    ListAdapter<UserBankAccountItemPresentation, BankOptionViewHolder>(
        object : DiffUtil.ItemCallback<UserBankAccountItemPresentation>() {
            override fun areItemsTheSame(
                oldItem: UserBankAccountItemPresentation,
                newItem: UserBankAccountItemPresentation
            ): Boolean {
                return oldItem.accountId == newItem.accountId
            }

            override fun areContentsTheSame(
                oldItem: UserBankAccountItemPresentation,
                newItem: UserBankAccountItemPresentation
            ): Boolean {
                return oldItem == newItem
            }

            override fun getChangePayload(
                oldItem: UserBankAccountItemPresentation,
                newItem: UserBankAccountItemPresentation
            ): Any? {
                return if (oldItem.accountId == newItem.accountId
                ) {
                    BankAccountOptionSelectPayload(newItem.selected)
                } else {
                    null
                }
            }

        }
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BankOptionViewHolder {
        val binding = ViewHolderBankAccountSelectBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return BankOptionViewHolder(binding, callback)
    }

    override fun onBindViewHolder(holder: BankOptionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onBindViewHolder(
        holder: BankOptionViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        val accountChangePayload =
            payloads.filterIsInstance<BankAccountOptionSelectPayload>().firstOrNull()
        if (accountChangePayload != null) {
            holder.onSelected(accountChangePayload.selected)
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    interface ViewHolderCallback {
        fun onSelected(selectedOption: UserBankAccountItemPresentation)
    }
}