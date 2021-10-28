package st.slex.messenger.auth.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import st.slex.messenger.auth.databinding.ActivityAuthBinding
import st.slex.messenger.auth.di.AuthComponent
import st.slex.messenger.auth.di.DaggerAuthComponent

@ExperimentalCoroutinesApi
class AuthActivity : AppCompatActivity() {

    private var _binding: ActivityAuthBinding? = null
    private val binding: ActivityAuthBinding
        get() = checkNotNull(_binding)

    private var _authComponent: AuthComponent? = null
    val authComponent: AuthComponent
        get() = checkNotNull(_authComponent)

    override fun onCreate(savedInstanceState: Bundle?) {
        _authComponent = DaggerAuthComponent.builder()
            .activity(this)
            .create()
        super.onCreate(savedInstanceState)
        _binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        _authComponent = null
    }
}