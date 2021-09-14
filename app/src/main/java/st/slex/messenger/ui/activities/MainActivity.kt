package st.slex.messenger.ui.activities

import android.Manifest.permission.READ_CONTACTS
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import st.slex.common.messenger.R
import st.slex.common.messenger.databinding.ActivityMainBinding
import st.slex.messenger.utilites.funs.appComponent
import st.slex.messenger.utilites.funs.setContacts
import st.slex.messenger.utilites.result.VoidResponse
import javax.inject.Inject

@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding get() = _binding!!
    private val viewModel: ActivityViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applicationContext.appComponent.inject(this)
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launchWhenResumed {
            this@MainActivity.setContacts {
                this.launch {
                    viewModel.updateContacts(it).collect()
                }
            }
        }
    }

    private fun VoidResponse.collector(): Unit =
        when (this) {
            is VoidResponse.Success -> {

            }
            is VoidResponse.Failure -> {

            }
            is VoidResponse.Loading -> {

            }
        }

    override fun onStart() {
        super.onStart()
        viewModel.changeState(getString(R.string.state_online))
    }

    override fun onStop() {
        super.onStop()
        viewModel.changeState(getString(R.string.state_offline))
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (ContextCompat.checkSelfPermission(
                this,
                READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            CoroutineScope(Dispatchers.IO).launch {
                this@MainActivity.setContacts {
                    lifecycleScope.launch {
                        viewModel.updateContacts(it).collect {
                            it.collector()
                        }
                    }
                }
            }
        }
    }

}


