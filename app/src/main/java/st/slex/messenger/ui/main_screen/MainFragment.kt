package st.slex.messenger.ui.main_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import st.slex.common.messenger.R
import st.slex.common.messenger.databinding.FragmentMainBinding
import st.slex.messenger.ui.main_screen.adapter.MainAdapter
import st.slex.messenger.ui.main_screen.model.MainScreenDatabase
import st.slex.messenger.ui.main_screen.model.MainScreenRepository
import st.slex.messenger.ui.main_screen.model.base.MainMessage
import st.slex.messenger.ui.main_screen.viewmodel.MainScreenViewModel
import st.slex.messenger.ui.main_screen.viewmodel.MainScreenViewModelFactory
import st.slex.messenger.utilites.Const.AUTH
import st.slex.messenger.utilites.restartActivity

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var navGraph: NavGraph
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MainAdapter
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private val database = MainScreenDatabase()
    private val repository by lazy { MainScreenRepository(database) }

    private val viewModel: MainScreenViewModel by viewModels {
        MainScreenViewModelFactory(repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initNavigationFields()
        setActionBar()
        initRecyclerView()
    }

    private fun setActionBar() {
        appBarConfiguration =
            AppBarConfiguration(setOf(R.id.nav_home), binding.mainScreenDrawerLayout)
        NavigationUI.setupWithNavController(
            binding.mainScreenToolbar,
            navController,
            appBarConfiguration
        )
        binding.navView.setupWithNavController(navController)
        binding.navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_nav_btn_sign_out -> {
                    AUTH.signOut()
                    requireActivity().restartActivity()
                }
            }
            false
        }
    }

    /*private fun setUserInfoInHeader() {
        val headerView = binding.navView.getHeaderView(0)
        val headerBinding = NavigationDrawerHeaderBinding.bind(headerView)
        activityViewModel.getUserForHeader.observe(this) {
            headerBinding.navigationHeaderImage.downloadAndSet(it.url)
            headerBinding.navigationHeaderUserName.text = it.username
            headerBinding.navigationHeaderPhoneNumber.text = it.phone
        }
    }*/

    private fun initNavigationFields() {
        navHostFragment =
            ((activity as AppCompatActivity).supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment)
        navController = navHostFragment.navController
        val navInflater = navHostFragment.navController.navInflater
        navGraph = navInflater.inflate(R.navigation.nav_graph)
    }

    private fun initRecyclerView() {
        recyclerView = binding.fragmentMainRecyclerView
        adapter = MainAdapter()
        layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager

        viewModel.mainMessage.observe(viewLifecycleOwner) {
            adapter.makeMainList(it as MutableList<MainMessage>)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}