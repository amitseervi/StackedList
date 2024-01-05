package com.amit.stackedlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.amit.stackedlist.databinding.FragmentHomeBinding

class HomeContentFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
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
        binding.ctaShowNextStack.setOnClickListener {
            binding.stackContainer.showNextChild()
        }
        binding.ctaShowPreviousStack.setOnClickListener {
            binding.stackContainer.showPreviousChild()
        }
    }
}