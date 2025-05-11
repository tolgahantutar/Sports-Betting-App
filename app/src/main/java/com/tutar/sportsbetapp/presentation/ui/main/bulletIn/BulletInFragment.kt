package com.tutar.sportsbetapp.presentation.ui.main.bulletIn

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.tutar.sportsbetapp.R
import com.tutar.sportsbetapp.core.util.Resource
import com.tutar.sportsbetapp.databinding.FragmentBulletInBinding
import com.tutar.sportsbetapp.presentation.extensions.createProgressDialog
import com.tutar.sportsbetapp.presentation.viewModel.BetBasketViewModel
import com.tutar.sportsbetapp.presentation.viewModel.MainViewModel
import com.tutar.sportsbetapp.presentation.viewModel.SelectedBet
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BulletInFragment : Fragment() {

    private var _binding: FragmentBulletInBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: EventsAdapter
    private val mainViewModel: MainViewModel by viewModels()
    private val betBasketViewModel: BetBasketViewModel by activityViewModels()
    private var blockingProgress: Dialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBulletInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_toolbar, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.queryHint = "Takım veya maç ara..."

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if ((newText?.length ?: 0) >= 2) {
                    adapter.filter(newText.orEmpty())
                } else {
                    adapter.filter("")
                }
                return true
            }
        })

        searchView.setOnCloseListener {
            adapter.filter("")
            false
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = EventsAdapter()
        binding.rvBulletIn.adapter = adapter
        binding.rvBulletIn.layoutManager = LinearLayoutManager(requireContext())

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.uiState.collect { state ->
                    when (state) {
                        is Resource.Loading -> {
                            blockingProgress = requireActivity().createProgressDialog()
                            blockingProgress?.let {
                                if (!it.isShowing) {
                                    it.show()
                                }
                            }
                        }
                        is Resource.Success -> {
                            blockingProgress?.dismiss()
                            adapter.updateItems(state.data)
                        }
                        is Resource.Error -> {
                            Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                            blockingProgress?.dismiss()
                        }
                        else -> {
                            //no-op
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                betBasketViewModel.selectedBets.collect { selectedBets ->
                    adapter.setSelectedBets(selectedBets)
                }
            }
        }

        adapter.onOddSelected = { event, eventId, isAlreadySelected, gameName, oddName, price, marketKey, point ->
            if (isAlreadySelected) {
                betBasketViewModel.removeBet(eventId)
            } else {
                betBasketViewModel.addOrReplaceBet(
                    SelectedBet(
                        eventId = eventId,
                        gameName = gameName,
                        oddName = oddName,
                        price = price,
                        marketKey = marketKey,
                        point = point,
                        homeTeam = event.homeTeam,
                        awayTeam = event.awayTeam
                    )
                )
            }
        }

        mainViewModel.fetchOdds()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}