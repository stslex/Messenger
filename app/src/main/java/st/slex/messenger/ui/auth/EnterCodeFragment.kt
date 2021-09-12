package st.slex.messenger.ui.auth

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.google.android.material.transition.MaterialContainerTransform
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import st.slex.common.messenger.R
import st.slex.common.messenger.databinding.FragmentEnterCodeBinding
import st.slex.messenger.ui.activities.MainActivity
import st.slex.messenger.utilites.base.BaseFragment
import st.slex.messenger.utilites.funs.showPrimarySnackBar
import st.slex.messenger.utilites.funs.start
import st.slex.messenger.utilites.result.AuthResponse

@ExperimentalCoroutinesApi
class EnterCodeFragment : BaseFragment() {

    private var _binding: FragmentEnterCodeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by viewModels {
        viewModelFactory.get()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.nav_host_fragment
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
            scrimColor = Color.TRANSPARENT
        }
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.title_code)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEnterCodeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: EnterCodeFragmentArgs by navArgs()
        val id = args.id
        binding.fragmentCodeTextInput.addTextChangedListener { code ->
            if (code?.length == 6) {
                binding.fragmentCodeProgressIndicator.visibility = View.VISIBLE
                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.sendCode(id = id, code = code.toString()).collect {
                        it.collector()
                    }
                }
            }
        }
    }

    private fun AuthResponse.collector() {
        when (this) {
            is AuthResponse.Success -> {
                Log.i("Response::code:", "Success")
                binding.root.showPrimarySnackBar(getString(R.string.snack_success))
                requireActivity().start(MainActivity())
            }
            is AuthResponse.Failure -> {
                Log.i("Response::code:", "Failure")
                binding.root.showPrimarySnackBar(exception.toString())
            }
            else -> {
                Log.e("EnterCode: Loading", "loading...")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

