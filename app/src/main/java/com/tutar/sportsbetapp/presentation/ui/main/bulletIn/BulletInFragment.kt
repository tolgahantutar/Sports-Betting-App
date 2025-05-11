package com.tutar.sportsbetapp.presentation.ui.main.bulletIn

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.tutar.sportsbetapp.core.util.Resource
import com.tutar.sportsbetapp.databinding.FragmentBulletInBinding
import com.tutar.sportsbetapp.presentation.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BulletInFragment : Fragment() {

    private lateinit var binding: FragmentBulletInBinding
    private lateinit var adapter: EventsAdapter
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBulletInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = EventsAdapter()
        binding.rvBulletIn.adapter = adapter
        binding.rvBulletIn.layoutManager = LinearLayoutManager(requireContext())

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.uiState.collect { state ->
                    when (state) {
                        is Resource.Loading -> {/* show loader */}
                        is Resource.Success -> {
                            adapter.updateItems(state.data)
                        }
                        is Resource.Error -> {/* show error */}
                    }
                }
            }
        }

        mainViewModel.fetchOdds()
    }
}