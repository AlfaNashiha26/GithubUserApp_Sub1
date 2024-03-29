package com.alfa.mygithubuserapp.activity

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.alfa.mygithubuserapp.R
import com.alfa.mygithubuserapp.adapter.ListSearchAdapter
import com.alfa.mygithubuserapp.databinding.ActivityMainBinding
import com.alfa.mygithubuserapp.response.ItemsSearch
import com.alfa.mygithubuserapp.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel : MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.rvResultUser.layoutManager = GridLayoutManager(this, 2)
        } else {
            binding.rvResultUser.layoutManager = LinearLayoutManager(this)
        }

        // Memuat data API pertama kali saat aplikasi dijalankan
        mainViewModel.fetchInitialData()

        mainViewModel.userList.observe(this) { users ->
            setUserData(users)
        }
        mainViewModel.userCount.observe(this) {
            binding.tvResultUser.text = resources.getString(R.string.tv_resultData, it)
        }
        mainViewModel.isLoading.observe(this) {
            showLoading(it) // Menampilkan indikator loading hanya saat memuat data dari API
        }
        mainViewModel.toastText.observe(this) {
            it.getContentIfNotHandled()?.let { toastText ->
                Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchInitialData() {
        // Memuat data API pertama kali saat aplikasi dijalankan
        mainViewModel.findUser("") // Anda dapat mengisi query sesuai kebutuhan
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                mainViewModel.findUser(query) // Melakukan pencarian ketika pengguna mengirimkan query
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return true
    }
    private fun setUserData(users: List<ItemsSearch>?) {
        val listUserAdapter = ListSearchAdapter(users as ArrayList<ItemsSearch>)
        binding.rvResultUser.adapter = listUserAdapter

        listUserAdapter.setOnItemClickCallback(object : ListSearchAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ItemsSearch) {
                showSelectedUser(data)
            }
        })
    }

    private fun showSelectedUser(user: ItemsSearch) {
        val detailUserIntent = Intent(this, DetailActivity::class.java)
        detailUserIntent.putExtra(DetailActivity.EXTRA_USER, user.login)
        startActivity(detailUserIntent)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progresBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}