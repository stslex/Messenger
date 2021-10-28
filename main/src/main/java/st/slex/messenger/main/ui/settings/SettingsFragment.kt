package st.slex.messenger.main.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import st.slex.messenger.core.Resource
import st.slex.messenger.main.R
import st.slex.messenger.main.databinding.FragmentSettingsBinding
import st.slex.messenger.main.ui.core.BaseFragment
import st.slex.messenger.main.utilites.funs.setSupportActionBar

@ExperimentalCoroutinesApi
class SettingsFragment : BaseFragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SettingsViewModel by viewModels { viewModelFactory.get() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.settingsToolbar.title = getString(R.string.title_contacts)
        setSupportActionBar(binding.settingsToolbar)
        binding.settingsSignOut.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.signOut(getString(R.string.state_offline)).collect {
                    it.collector
                }
            }
        }
    }

    private val Resource<Nothing?>.collector
        get() = when (this) {
            is Resource.Success -> {
                val intent = Intent().setClassName(
                    requireContext(),
                    "st.slex.messenger.auth.ui.AuthActivity"
                )
                startActivity(intent)
                requireActivity().finish()
            }
            is Resource.Failure -> {
            } //TODO
            is Resource.Loading -> {
            } //TODO
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}