package com.amit.stackedlist

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.amit.stackedlist.adapter.BankAccountOptionAdapter
import com.amit.stackedlist.adapter.EmiRateOptionAdapter
import com.amit.stackedlist.databinding.FragmentHomeBinding
import com.amit.stackedlist.model.ui.EmiRateOptionPresentationItem
import com.amit.stackedlist.model.ui.UserBankAccountItemPresentation
import com.amit.stackedlist.repository.DummyEmiRateOptionRepository
import com.amit.stackedlist.repository.DummyUserBankAccountRepository
import com.amit.stackedlist.view.StackItemView
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeContentFragment : Fragment(), EmiRateOptionAdapter.ViewHolderCallback,
    BankAccountOptionAdapter.ViewHolderCallback {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var factory: HomeViewModel.Factory
    private val viewModel: HomeViewModel by lazy {
        ViewModelProvider(this@HomeContentFragment, factory)[HomeViewModel::class.java]
    }

    private val stackCallbackEmiCreditSelect = object : StackItemView.Callback {
        override fun onStackExpanded() {
            viewModel.onEmiValueSelectSectionVisible()
        }
    }

    private val stackCallbackEmiRateSelect = object : StackItemView.Callback {
        override fun onStackExpanded() {
            viewModel.onEmiRateOptionSelectSectionVisible()
        }
    }

    private val stackCallbackBankAccountSelect = object : StackItemView.Callback {
        override fun onStackExpanded() {
            viewModel.onBankAccountSelectSectionVisible()
        }
    }

    private lateinit var emiOptionAdapter: EmiRateOptionAdapter
    private lateinit var bankAccountOptionAdapter: BankAccountOptionAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        factory = HomeViewModel.Factory(
            requireContext().applicationContext as Application,
            DummyEmiRateOptionRepository(),
            DummyUserBankAccountRepository()
        )
        binding.ctaButton.setOnClickListener {
            binding.stackContainer.showNextChild()
        }

        bindCreditExpandedView()
        observeCreditSelection()
        observeCtaMessage()
        observeCtaEnableState()
        bindStackCallbacks()
        bindEmiOptionRateAdapter()
        bindUserBankAccountAdapter()
        observeEmiRateOptionList()
        observeBankAccountOptionList()
        observeEmiSelectedRateOption()
        bindAddBankAccountCta()
    }


    private fun bindEmiOptionRateAdapter() {
        emiOptionAdapter = EmiRateOptionAdapter(this)
        binding.expandedEmiOptionSelect.recyclerViewPayOptions.adapter = emiOptionAdapter
    }

    private fun bindUserBankAccountAdapter() {
        bankAccountOptionAdapter = BankAccountOptionAdapter(this)
        binding.expandedSelectBankAccount.bankAccountsRv.adapter = bankAccountOptionAdapter
    }

    private fun observeCreditSelection() {
        lifecycleScope.launch {
            viewModel.selectedCredit.collectLatest {
                binding.collapsedEmiSelect.creditAmountValue.text = it
                binding.expandedEmiSelect.creditInputTv.text = it
            }
        }
    }

    private fun observeCtaMessage() {
        lifecycleScope.launch {
            viewModel.ctaButtonMessage.collectLatest {
                binding.ctaButton.text = it
            }
        }
    }

    private fun observeCtaEnableState() {
        lifecycleScope.launch {
            viewModel.ctaButtonEnabled.collectLatest {
                binding.ctaButton.isEnabled = it
            }
        }
    }

    private fun observeEmiRateOptionList() {
        lifecycleScope.launch {
            viewModel.emiRateOptionList.collectLatest {
                emiOptionAdapter.submitList(it)
            }
        }
    }

    private fun observeBankAccountOptionList() {
        lifecycleScope.launch {
            viewModel.userBankAccountOptionList.collectLatest {
                bankAccountOptionAdapter.submitList(it)
            }
        }
    }

    private fun observeEmiSelectedRateOption() {
        lifecycleScope.launch {
            viewModel.emiRateSelectedOption.collectLatest {
                if (it == null) {
                    binding.collapsedEmiOptionSelect.emiValue.text = getString(R.string.na)
                    binding.collapsedEmiOptionSelect.durationValue.text = getString(R.string.na)
                } else {
                    binding.collapsedEmiOptionSelect.emiValue.text = it.amountPerMonth
                    binding.collapsedEmiOptionSelect.durationValue.text = it.duration
                }
            }
        }
    }

    private fun bindCreditExpandedView() {
        binding.expandedEmiSelect.number0.setOnClickListener {
            viewModel.onCreditInputNumberPressed(0)
        }
        binding.expandedEmiSelect.number1.setOnClickListener {
            viewModel.onCreditInputNumberPressed(1)
        }
        binding.expandedEmiSelect.number2.setOnClickListener {
            viewModel.onCreditInputNumberPressed(2)
        }
        binding.expandedEmiSelect.number3.setOnClickListener {
            viewModel.onCreditInputNumberPressed(3)
        }
        binding.expandedEmiSelect.number4.setOnClickListener {
            viewModel.onCreditInputNumberPressed(4)
        }
        binding.expandedEmiSelect.number5.setOnClickListener {
            viewModel.onCreditInputNumberPressed(5)
        }
        binding.expandedEmiSelect.number6.setOnClickListener {
            viewModel.onCreditInputNumberPressed(6)
        }
        binding.expandedEmiSelect.number7.setOnClickListener {
            viewModel.onCreditInputNumberPressed(7)
        }
        binding.expandedEmiSelect.number8.setOnClickListener {
            viewModel.onCreditInputNumberPressed(8)
        }
        binding.expandedEmiSelect.number9.setOnClickListener {
            viewModel.onCreditInputNumberPressed(9)
        }
        binding.expandedEmiSelect.backSpace.setOnClickListener {
            viewModel.onCreditInputBackSpacePressed()
        }
    }

    private fun bindStackCallbacks() {
        binding.stackItem1.setCallback(stackCallbackEmiCreditSelect)
        binding.stackItem2.setCallback(stackCallbackEmiRateSelect)
        binding.stackItem3.setCallback(stackCallbackBankAccountSelect)
    }

    private fun bindAddBankAccountCta() {
        binding.expandedSelectBankAccount.addAccountCta.setOnClickListener {
            Toast.makeText(
                requireContext(),
                getString(R.string.not_implemented),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onSelected(selectedOption: EmiRateOptionPresentationItem) {
        viewModel.onEmiRateOptionSelected(selectedOption)
    }

    override fun onClickDetail(selectedOption: EmiRateOptionPresentationItem) {
        Toast.makeText(context, getString(R.string.not_implemented), Toast.LENGTH_SHORT).show()
    }

    override fun onSelected(selectedOption: UserBankAccountItemPresentation) {
        viewModel.onBankAccountSelected(selectedOption)
    }
}