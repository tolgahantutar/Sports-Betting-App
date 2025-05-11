package com.tutar.sportsbetapp.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tutar.sportsbetapp.core.util.Resource
import com.tutar.sportsbetapp.domain.usecase.AuthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUseCase: AuthUseCase
): ViewModel() {

    private val _authState = MutableStateFlow<Resource<Unit>?>(null)
    val authState: StateFlow<Resource<Unit>?> = _authState

    private val _isLoggedIn = MutableStateFlow<Boolean?>(null)
    val isLoggedIn: StateFlow<Boolean?> = _isLoggedIn

    fun login(email: String, password: String) {
        viewModelScope.launch {
            authUseCase.login(email, password).collect {
                _authState.value = it
            }
        }
    }

    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            authUseCase.signUp(email, password).collect {
                _authState.value = it
            }
        }
    }

    fun checkLoginStatus() {
        _isLoggedIn.value = authUseCase.checkIsUserLoggedIn()
    }
}