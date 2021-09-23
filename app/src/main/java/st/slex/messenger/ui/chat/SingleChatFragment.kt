package st.slex.messenger.ui.chat

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialContainerTransform
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import st.slex.common.messenger.R
import st.slex.common.messenger.databinding.FragmentSingleChatBinding
import st.slex.messenger.core.Response
import st.slex.messenger.data.chat.MessageModel
import st.slex.messenger.ui.chat.adapter.ChatAdapter
import st.slex.messenger.ui.core.BaseFragment
import st.slex.messenger.ui.user_profile.UserUI
import st.slex.messenger.ui.user_profile.UserUiResult


@ExperimentalCoroutinesApi
class SingleChatFragment : BaseFragment() {

    private var _binding: FragmentSingleChatBinding? = null
    private val binding get() = _binding!!

    private lateinit var uid: String

    private lateinit var recycler: RecyclerView
    private lateinit var adapter: ChatAdapter
    private lateinit var layoutManager: LinearLayoutManager

    private var countMessage = 10
    private var isScrolling = false
    private var isScrollToPosition = true
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private val viewModel: SingleChatViewModel by viewModels {
        viewModelFactory.get()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.nav_host_fragment
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
            scrimColor = Color.TRANSPARENT
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSingleChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        takeExtras()
        NavigationUI.setupWithNavController(
            binding.chatToolbar,
            findNavController(),
            AppBarConfiguration(setOf(R.id.nav_contact))
        )
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getUser(uid).collect {

            }
        }
        initRecyclerView()
    }

    private fun initRecyclerView() {
        recycler = binding.singleChatRecycler
        adapter = ChatAdapter(FirebaseAuth.getInstance().uid.toString())
        layoutManager = LinearLayoutManager(requireContext())
        recycler.layoutManager = layoutManager
        recycler.adapter = adapter
        recycler.isNestedScrollingEnabled = false
        swipeRefreshLayout = binding.singleChatRefreshLayout
        viewModel.getMessages(uid, countMessage).observe(viewLifecycleOwner, messageObserver)
        recycler.addOnScrollListener(scrollListener)
        swipeRefreshLayout.setOnRefreshListener {
            updateData()
        }
    }

    private val messageObserver: Observer<Response<MessageModel>> = Observer {
        when (it) {
            is Response.Success -> {
                if (isScrollToPosition) {
                    adapter.addItemToBottom(it.value) {
                        recycler.smoothScrollToPosition(adapter.itemCount)
                    }
                } else {
                    adapter.addItemToTop(it.value) {
                        swipeRefreshLayout.isRefreshing = false
                    }
                }
            }
            is Response.Failure -> {

            }
            is Response.Loading -> {

            }
        }
    }

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (isScrolling
                && dy < 0
                && layoutManager.findFirstVisibleItemPosition() <= 3
            ) {
                updateData()
            }
        }
    }

    private fun updateData() {
        isScrollToPosition = false
        isScrolling = false
        countMessage += 10
    }

    private fun takeExtras() {
        val args: SingleChatFragmentArgs by navArgs()
        val id = args.id
        uid = id
        binding.toolbarInfo.toolbarInfoCardView.transitionName = uid
        glide.setImage(
            binding.toolbarInfo.shapeableImageView,
            args.url,
            needCircleCrop = true,
            needCrop = true
        )
    }

    private fun UserUiResult.collect() {
        when (this) {
            is UserUiResult.Success -> {
                data.mapChat(
                    userName = binding.toolbarInfo.usernameTextView,
                    stateText = binding.toolbarInfo.stateTextView
                )
                binding.singleChatRecyclerButton.setOnClickListener(data.sendClicker)
            }
            is UserUiResult.Failure -> {
                Log.i("Failure User in Chat", exception.message, exception.cause)
            }
            is UserUiResult.Loading -> {
                /*Start response*/
            }
        }
    }

    private val UserUI.sendClicker: View.OnClickListener
        get() = View.OnClickListener {
            isScrollToPosition = true
            val message = binding.singleChatRecyclerTextInput.editText?.text.toString()
            if (message.isEmpty()) {
                val snackBar =
                    Snackbar.make(binding.root, "Empty message", Snackbar.LENGTH_SHORT)
                snackBar.anchorView = binding.singleChatRecyclerTextInput
                snackBar.setAction("Ok") {}
                snackBar.show()
            } else {
                viewModel.sendMessage(message, this)
                binding.singleChatRecyclerTextInput.editText?.setText("")
            }
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}