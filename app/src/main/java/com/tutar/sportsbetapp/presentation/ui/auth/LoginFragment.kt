package com.tutar.sportsbetapp.presentation.ui.auth

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.tutar.sportsbetapp.R
import com.tutar.sportsbetapp.core.util.Resource
import com.tutar.sportsbetapp.databinding.FragmentLoginBinding
import com.tutar.sportsbetapp.presentation.extensions.createProgressDialog
import com.tutar.sportsbetapp.presentation.ui.main.MainActivity
import com.tutar.sportsbetapp.presentation.viewModel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val authViewmodel: AuthViewModel by viewModels()
    private var blockingProgress: Dialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val imeVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
            val imeHeight = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom

            binding.tvBecomeMemberNow.translationY = if (imeVisible) -imeHeight.toFloat() else 0f
            binding.tvNotMemberYet.translationY = if (imeVisible) -imeHeight.toFloat() else 0f

            insets
        }

        initTextValues()
        initClickListeners()
        initInputListeners()
        initObservers()
    }

    private fun initTextValues() {
        binding.tvNotMemberYet.text = getString(R.string.not_a_member, getString(R.string.app_name))
    }

    private fun initClickListeners() {
        binding.btnLogin.setOnClickListener {
            if (isValidate()) {
                authViewmodel.login(binding.tieEmail.text.toString(), binding.tiePassword.text.toString())
            }
        }

        binding.tvBecomeMemberNow.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
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

    private fun initInputListeners() {
        val watcher = {
            val emailFilled = !binding.tieEmail.text.isNullOrBlank()
            val passwordFilled = binding.tiePassword.text.toString().length >= 6
            binding.btnLogin.isEnabled = emailFilled && passwordFilled
        }

        binding.tieEmail.addTextChangedListener{watcher()}
        binding.tiePassword.addTextChangedListener{watcher()}
    }

    private fun initObservers() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                authViewmodel.authState.collect { state ->
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
                            navigateToMainActivity()
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

    private fun navigateToMainActivity() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}