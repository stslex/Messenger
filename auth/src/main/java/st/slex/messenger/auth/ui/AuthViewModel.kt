package st.slex.messenger.auth.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import st.slex.messenger.auth.domain.interf.AuthInteractor
import st.slex.messenger.auth.domain.interf.LoginDomainMapper
import st.slex.messenger.auth.ui.core.LoginUIResult
import javax.inject.Inject

@ExperimentalCoroutinesApi
class AuthViewModel @Inject constructor(
    private val interactor: AuthInteractor,
    private val mapper: LoginDomainMapper
) : ViewModel() {

    suspend fun login(phone: String): StateFlow<LoginUIResult> = interactor.login(phone)
        .flatMapLatest { flowOf(mapper.map(it)) }
        .flowOn(Dispatchers.IO)
        .stateIn(
            viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = LoginUIResult.Loading
        )

    suspend fun sendCode(
        id: String,
        code: String
    ): StateFlow<LoginUIResult> = interactor.sendCode(id, code)
        .flatMapLatest { flowOf(mapper.map(it)) }
        .flowOn(Dispatchers.IO)
        .stateIn(
            viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = LoginUIResult.Loading
        )
}