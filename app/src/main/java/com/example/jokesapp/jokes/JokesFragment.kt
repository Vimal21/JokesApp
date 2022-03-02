package com.example.jokesapp.jokes

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.jokesapp.R
import com.example.jokesapp.databinding.FragmentJokesBinding
import com.example.jokesapp.jokes.adapter.JokesAdapter
import com.example.jokesapp.jokes.viewModel.JokesViewModel
import com.example.jokesapp.utils.obtainViewModel

class JokesFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var fragmentJokesBinding: FragmentJokesBinding
    private var searchMenuItems : MenuItem? = null
    private var searchView : SearchView? = null
    private var handler : Handler? = null
    private var searchedJokes : String = ""
    private var searchRunnable : Runnable = Runnable {
        if(context == null)
            return@Runnable
        onRefresh()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentJokesBinding = FragmentJokesBinding.inflate(inflater, container, false).apply {
            jokesViewModel = (activity as AppCompatActivity).obtainViewModel(JokesViewModel::class.java).apply {
                dataLoading.observe(viewLifecycleOwner, { event ->
                    if (event != null)
                        updateProgressView(event.peekContent())
                })

                toastMessage.observe(this@JokesFragment, {
                        message ->
                    run {
                        if (message != null)
                            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                    }
                })
            }
        }
        setHasOptionsMenu(true)
        return fragmentJokesBinding.root
    }

    private fun updateProgressView(isLoading : Boolean) {
        fragmentJokesBinding.jokesSwipeRefreshLayout.isRefreshing = isLoading
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentJokesBinding.lifecycleOwner = this.viewLifecycleOwner
        setupListAdapter()
        handler = Handler(Looper.getMainLooper())
        fragmentJokesBinding.jokesSwipeRefreshLayout.setColorSchemeColors(Color.BLACK)
        fragmentJokesBinding.jokesSwipeRefreshLayout.setOnRefreshListener(this)
        onRefresh()
    }

    private fun setupListAdapter() {
        val viewModel = fragmentJokesBinding.jokesViewModel
        if (viewModel != null) {
            fragmentJokesBinding.jokeListRecyclerView.adapter = JokesAdapter()
        } else {
            Log.w(JokesFragment::class.java.name, "ViewModel not initialized when attempting to set up adapter.")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.search_menu, menu)
        searchMenuItems = menu.findItem(R.id.action_search)
        searchView = searchMenuItems?.actionView as SearchView
        super.onCreateOptionsMenu(menu, inflater)
        setSearchViewListener()
    }

    private fun setSearchViewListener() {
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(typedText: String): Boolean {
                searchedJokes = typedText
                if(handler != null) {
                    handler?.removeCallbacks(searchRunnable)
                    handler?.postDelayed(searchRunnable, 800)
                }
                return true
            }

        })
    }

    override fun onDestroy() {
        super.onDestroy()
        if(searchView?.isIconified == false){
            searchMenuItems?.collapseActionView()
        }
        if(handler != null) {
            handler?.removeCallbacks(searchRunnable)
        }
    }

    override fun onRefresh() {
        fragmentJokesBinding.jokesViewModel?.getJokeList(requireContext(), searchedJokes)
    }

    companion object {
        fun newInstance() = JokesFragment()
    }
}