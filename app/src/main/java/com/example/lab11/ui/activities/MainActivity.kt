package com.example.lab11.ui.activities

import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.lab11.R
import com.example.lab11.databinding.ActivityMainBinding
import com.example.lab11.ui.viewmodels.TaskViewModel
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private lateinit var taskViewModel: TaskViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        setupActionBarWithNavController(navController)

        taskViewModel = ViewModelProvider(this)[TaskViewModel::class.java]
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        val rootView = findViewById<View>(android.R.id.content)
        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                taskViewModel.setSearchQuery(newText.toString())
                return true
            }
        })
        val sortItem = menu.findItem(R.id.action_sort)
        sortItem.setOnMenuItemClickListener {
            taskViewModel.toggleSortOrder()
            val sortMessage = if (taskViewModel.getSortOrder()) "Sort by ascending date" else "Sort by descending date"
            Snackbar.make(rootView, sortMessage, Snackbar.LENGTH_SHORT).show()
            true
        }

        return super.onCreateOptionsMenu(menu)
    }

    // Handle showing and hiding SearchView based on which fragment is visible
    fun showToolbarIcons(isVisible: Boolean) {
        val menuItemSearch = binding.appBarMain.toolbar.menu.findItem(R.id.action_search)
        val menuItemSort = binding.appBarMain.toolbar.menu.findItem(R.id.action_sort)
        menuItemSearch?.isVisible = isVisible
        menuItemSort?.isVisible = isVisible
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}