package com.tutar.sportsbetapp.presentation.ui.main.profile


import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.tutar.sportsbetapp.core.util.Resource
import com.tutar.sportsbetapp.databinding.FragmentProfileBinding
import com.tutar.sportsbetapp.presentation.extensions.createProgressDialog
import com.tutar.sportsbetapp.presentation.ui.auth.AuthActivity
import com.tutar.sportsbetapp.presentation.viewModel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val profileViewModel: ProfileViewModel by viewModels()
    private var blockingProgress: Dialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnLogout.setOnClickListener {
            profileViewModel.signOut()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                profileViewModel.signOutState.collect { state ->
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
                            profileViewModel.resetState()
                            navigateToAuth()
                        }
                        is Resource.Error -> {
                            blockingProgress?.dismiss()
                            Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                            profileViewModel.resetState()
                        }
                        else -> Unit
                    }
                }
            }
        }
    }

    private fun navigateToAuth() {
        val intent = Intent(requireContext(), AuthActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

