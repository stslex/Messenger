package st.slex.messenger.domain.auth

sealed class LoginDomainResult {
    object Success : LoginDomainResult()
    class SendCode(val id: String) : LoginDomainResult()
    class Failure(val exception: Exception) : LoginDomainResult()
}