package com.tutar.sportsbetapp.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tutar.sportsbetapp.core.util.Resource
import com.tutar.sportsbetapp.domain.usecase.SignOutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val signOutUseCase: SignOutUseCase
) : ViewModel() {

    private val _signOutState = MutableStateFlow<Resource<Unit>?>(null)
    val signOutState: StateFlow<Resource<Unit>?> = _signOutState

    fun signOut() {
        viewModelScope.launch {
            signOutUseCase().collect { result ->
                _signOutState.value = result
            }
        }
    }

    fun resetState() {
        _signOutState.value = null
    }
}
