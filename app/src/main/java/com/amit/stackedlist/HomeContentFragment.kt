package com.amit.stackedlist

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.amit.stackedlist.databinding.FragmentHomeBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeContentFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var factory: HomeViewModel.Factory
    private val viewModel: HomeViewModel by lazy {
        ViewModelProvider(this@HomeContentFragment, factory)[HomeViewModel::class.java]
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
        observeCreditLimit()
    }

    private fun observeCreditLimit() {
        lifecycleScope.launch {
            viewModel.creditLimit.collectLatest {
                binding.expandedEmiSelect.seekerStartLabel.text =
                    getString(R.string.dollar_value, it.first.toString())
                binding.expandedEmiSelect.seekerEndLabel.text =
                    getString(R.string.dollar_value, it.second.toString())
            }
        }
    }

    private fun observeCreditSelection() {
        lifecycleScope.launch {
            viewModel.selectedCredit.collectLatest {
                binding.collapsedEmiSelect.creditAmountValue.text =
                    getString(R.string.dollar_value, it.toString())
                binding.expandedEmiSelect.creditAmountValue.text =
                    getString(R.string.dollar_value, it.toString())
            }
        }
    }

    private fun bindCreditExpandedView() {
        binding.expandedEmiSelect.seeker.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                viewModel.onEmiAmountSeekerChange(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })
    }
}