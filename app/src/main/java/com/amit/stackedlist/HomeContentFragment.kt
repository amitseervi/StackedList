package com.amit.stackedlist

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.amit.stackedlist.databinding.FragmentHomeBinding
import com.amit.stackedlist.view.StackItemView
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeContentFragment : Fragment() {
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
            viewModel.onBankAccountSelectSetionVisible()
        }
    }

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
        factory = HomeViewModel.Factory(requireContext().applicationContext as Application)
        binding.ctaButton.setOnClickListener {
            binding.stackContainer.showNextChild()
        }

        bindCreditExpandedView()
        observeCreditSelection()
        observeCtaMessage()
        observeCtaEnableState()
        bindStackCallbacks()
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
}