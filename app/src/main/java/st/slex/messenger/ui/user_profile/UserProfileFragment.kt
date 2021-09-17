package st.slex.messenger.ui.user_profile

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialContainerTransform
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import st.slex.common.messenger.R
import st.slex.common.messenger.databinding.FragmentUserProfileBinding
import st.slex.messenger.ui.core.BaseFragment

@ExperimentalCoroutinesApi
class UserProfileFragment : BaseFragment() {

    private var _binding: FragmentUserProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UserViewModel by viewModels { viewModelFactory.get() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.nav_host_fragment
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
            scrimColor = Color.TRANSPARENT
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.currentUser().collect {
                it.collector()
            }
        }
    }

    private fun UserUiResult.collector() {
        when (this) {
            is UserUiResult.Success -> {
                binding.profileProgress.visibility = View.GONE
                this.data.mapProfile(
                    phoneNumber = binding.phoneTextView,
                    userName = binding.usernameTextView,
                    avatar = binding.avatarImageView,
                    bioText = binding.bioTextView,
                    fullName = binding.fullNameTextView,
                    usernameCard = binding.usernameCardView
                )

                binding.usernameCardView.setOnClickListener {
                    data.changeUsername { card, username ->
                        val directions = UserProfileFragmentDirections
                            .actionNavUserProfileToEditUsernameFragment(username)
                        val extras = FragmentNavigatorExtras(card to card.transitionName)
                        findNavController().navigate(directions, extras)
                    }
                }
            }
            is UserUiResult.Loading -> {
                binding.profileProgress.visibility = View.VISIBLE
            }
            is UserUiResult.Failure -> {
                binding.profileProgress.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}