package com.st.slex.common.messenger.contacts

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.transition.MaterialContainerTransform
import com.st.slex.common.messenger.R
import com.st.slex.common.messenger.contacts.adapter.ContactAdapter
import com.st.slex.common.messenger.contacts.model.ContactRepository
import com.st.slex.common.messenger.contacts.viewmodel.ContactViewModel
import com.st.slex.common.messenger.contacts.viewmodel.ContactViewModelFactory
import com.st.slex.common.messenger.databinding.FragmentContactBinding

class ContactFragment : Fragment() {

    private lateinit var binding: FragmentContactBinding

    private lateinit var navGraph: NavGraph
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    private val repository = ContactRepository()
    private val contactViewModel: ContactViewModel by viewModels {
        ContactViewModelFactory(repository)
    }

    private lateinit var recycler: RecyclerView
    private lateinit var adapter: ContactAdapter
    private lateinit var layoutManager: StaggeredGridLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContactBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        contactViewModel.initContact()
        initRecyclerView()
        setHasOptionsMenu(true)
        setTransitAnimation()
        initNavigationFields()
        setActionBar()
    }

    private fun setTransitAnimation() {
        enterTransition = MaterialContainerTransform().apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }
    }

    private fun setActionBar() {
        val toolbar = binding.fragmentContactToolbar
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        toolbar.title = "Contacts"
        appBarConfiguration = AppBarConfiguration(setOf(R.id.nav_home))
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration)
    }

    private fun initNavigationFields() {
        navHostFragment =
            ((activity as AppCompatActivity).supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment)
        navController = navHostFragment.navController
        val navInflater = navHostFragment.navController.navInflater
        navGraph = navInflater.inflate(R.navigation.nav_graph)
    }

    private fun initRecyclerView() {
        recycler = binding.fragmentContactRecycler
        adapter = ContactAdapter(clickListener)
        layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        contactViewModel.contactList.observe(viewLifecycleOwner) {
            adapter.addItems(it)
            Log.i("Transit::List", it.toString())
        }

        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(requireContext())

        postponeEnterTransition()
        recycler.doOnPreDraw {
            startPostponedEnterTransition()
        }
    }

    private val clickListener = ContactClickListener { cardView, contact, key ->
        val directions = ContactFragmentDirections.actionNavContactToNavSingleChat(contact, key)
        cardView.isTransitionGroup = true
        cardView.transitionName = key
        val extras = FragmentNavigatorExtras(cardView to cardView.transitionName)
        Log.i("Transit::Contact", key)

        findNavController().navigate(directions, extras)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_contact_appbar, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
}