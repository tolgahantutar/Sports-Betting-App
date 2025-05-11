package com.tutar.sportsbetapp.presentation.ui.auth

import android.app.Dialog
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.tutar.sportsbetapp.R
import com.tutar.sportsbetapp.core.util.Resource
import com.tutar.sportsbetapp.databinding.FragmentSignUpBinding
import com.tutar.sportsbetapp.presentation.extensions.createProgressDialog
import com.tutar.sportsbetapp.presentation.viewModel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private val authViewModel: AuthViewModel by viewModels()
    private var blockingProgress: Dialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClickListeners()
        initInputListeners()
        initObservables()
    }

    private fun initInputListeners() {
        with(binding) {
            val watcher = {
                val nameFilled = tieName.text.toString().length >= 2
                val surnameFilled = tieSurname.text.toString().length >= 2
                val emailFilled = !tieEmail.text.isNullOrBlank()
                val passwordFilled = tiePassword.text.toString().length >= 6


                btnSignUp.isEnabled = nameFilled && surnameFilled && emailFilled && passwordFilled
            }

            tieName.addTextChangedListener { watcher() }
            tieSurname.addTextChangedListener { watcher() }
            tieEmail.addTextChangedListener{watcher()}
            tiePassword.addTextChangedListener{watcher()}
        }
    }

    private fun initClickListeners() {
        binding.btnSignUp.setOnClickListener {
            if (isValidate()) {
                authViewModel.signUp(
                    binding.tieEmail.text.toString(),
                    binding.tiePassword.text.toString()
                )
            }
        }
    }

    private fun initObservables() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                authViewModel.authState.collect { state ->
                    when(state) {
                        Resource.Loading -> {
                            blockingProgress = requireActivity().createProgressDialog()
                            blockingProgress?.let {
                                if (!it.isShowing) {
                                    it.show()
                                }
                            }
                        }
                        is Resource.Success -> {
                            blockingProgress?.dismiss()
                            Toast.makeText(requireContext(), "Signed in successfully", Toast.LENGTH_SHORT).show()
                            findNavController().popBackStack()
                        }
                        is Resource.Error -> {
                            blockingProgress?.dismiss()
                            Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            //no-op
                        }
                    }
                }
            }
        }
    }

    private fun isValidate(): Boolean {
        var hasError = false

        if (Patterns.EMAIL_ADDRESS.matcher(binding.tieEmail.text.toString()).matches()) {
            binding.tilEmail.showError(null)
        } else {
            binding.tilEmail.showError(getString(R.string.please_enter_valid_e_mail))
            hasError = true
        }

        return !hasError
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}