package st.slex.messenger.ui.auth.engine.interf

import android.app.Activity
import kotlinx.coroutines.flow.Flow
import st.slex.messenger.utilites.result.AuthResponse

interface LoginEngine {
    suspend fun login(phone: String, activity: Activity): Flow<AuthResponse>
}