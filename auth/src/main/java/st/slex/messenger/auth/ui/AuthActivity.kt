package st.slex.messenger.auth.ui

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import st.slex.messenger.auth.databinding.ActivityAuthBinding
import st.slex.messenger.auth.di.AuthComponent
import st.slex.messenger.auth.di.DaggerAuthComponent

class AuthActivity : AppCompatActivity() {

    private var _binding: ActivityAuthBinding? = null
    private val binding: ActivityAuthBinding
        get() = checkNotNull(_binding)

    val authComponent: AuthComponent by lazy {
        DaggerAuthComponent.builder().activity(this).create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

@ExperimentalCoroutinesApi
val Context.authComponent: AuthComponent
    get() = when (this) {
        is AuthActivity -> authComponent
        else -> (this as FragmentActivity).authComponent
    }