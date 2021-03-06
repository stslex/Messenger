package st.slex.messenger.main.data.settings

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import st.slex.core.FirebaseConstants.CHILD_STATE
import st.slex.core.FirebaseConstants.NODE_USER
import st.slex.core.Resource
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

interface SettingsRepository {

    suspend fun signOut(): Resource<Nothing?>

    @ExperimentalCoroutinesApi
    class Base @Inject constructor(
        reference: DatabaseReference,
        user: FirebaseUser
    ) : SettingsRepository {

        private val signOutReference by lazy {
            reference.child(NODE_USER).child(user.uid).child(CHILD_STATE)
        }

        override suspend fun signOut(): Resource<Nothing?> = suspendCoroutine { continuation ->
            val task: Task<Void> = signOutReference.setValue(SIGN_OUT_STATE)
            task.addOnCompleteListener {
                if (it.isSuccessful) {
                    Firebase.auth.signOut()
                    continuation.resumeWith(Result.success(Resource.Success(null)))
                } else {
                    continuation.resumeWith(Result.success(Resource.Failure(it.exception!!)))

                }
            }
        }

        companion object {
            private const val SIGN_OUT_STATE: String = "Offline"
        }
    }
}