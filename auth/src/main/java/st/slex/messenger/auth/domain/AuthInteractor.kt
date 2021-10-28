package st.slex.messenger.auth.domain

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import st.slex.messenger.auth.data.AuthRepository
import st.slex.messenger.auth.ui.LoginEngine
import st.slex.messenger.auth.ui.SendCodeEngine
import st.slex.messenger.core.Resource
import javax.inject.Inject

interface AuthInteractor {

    suspend fun login(phone: String): Flow<LoginDomainResult>
    suspend fun sendCode(id: String, code: String): Flow<LoginDomainResult>

    @InternalCoroutinesApi
    @ExperimentalCoroutinesApi
    class Base @Inject constructor(
        private val repository: AuthRepository,
        private val loginEngine: LoginEngine,
        private val sendCodeEngine: SendCodeEngine,
    ) : AuthInteractor {

        override suspend fun login(phone: String): Flow<LoginDomainResult> = flow {
            emit(loginEngine.login(phone))
        }.checkAuth()

        override suspend fun sendCode(id: String, code: String): Flow<LoginDomainResult> = flow {
            emit(sendCodeEngine.sendCode(id, code))
        }.checkAuth()

        private suspend fun Flow<LoginDomainResult>.checkAuth(): Flow<LoginDomainResult> = flow {
            this@checkAuth.collect {
                if (it is LoginDomainResult.Success.LogIn) {
                    repository.saveUser().collect { saveResult ->
                        when (saveResult) {
                            is Resource.Success -> emit(LoginDomainResult.Success.LogIn)
                            is Resource.Failure -> emit(LoginDomainResult.Failure(saveResult.exception))
                            is Resource.Loading -> {
                                //TODO
                            }
                        }
                    }
                } else {
                    emit(it)
                }
            }
        }
    }
}