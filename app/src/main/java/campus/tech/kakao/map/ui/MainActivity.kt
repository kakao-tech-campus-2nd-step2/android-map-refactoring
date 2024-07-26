package campus.tech.kakao.map.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import campus.tech.kakao.map.adapter.Adapter
import campus.tech.kakao.map.data.AppDatabase
import campus.tech.kakao.map.databinding.ActivityMainBinding
import campus.tech.kakao.map.utility.SaveHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var db: AppDatabase

    @Inject
    lateinit var searchSave: SaveHelper

    private val viewModel: MainViewModel by viewModels()

    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var adapter: Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        mainBinding.recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = Adapter(mutableListOf())
        mainBinding.recyclerView.adapter = adapter

        viewModel.profiles.observe(this, Observer { profiles ->
            if (profiles.isEmpty()) {
                showNoResults()
            } else {
                adapter.updateProfiles(profiles)
                mainBinding.tvNoResult.visibility = View.GONE

                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        db.profileDao().insertAll(*profiles.toTypedArray())
                    }
                }
            }
        })

        viewModel.noResult.observe(this, Observer { noResult ->
            if (noResult) {
                showNoResults()
            }
        })

        mainBinding.etSearch.addTextChangedListener { text ->
            val search = text.toString()
            if (search.isNotEmpty()) {
                viewModel.searchProfiles(search)
            } else {
                showNoResults()
            }
        }

        adapter.setOnItemClickListener(object : Adapter.OnItemClickListener {
            override fun onItemClick(name: String, address: String, latitude: String, longitude: String) {
                if (searchSave.isProfileInSearchSave(name, mainBinding.llSave)) {
                    searchSave.removeSavedItem(name, mainBinding.llSave)
                }
                searchSave.addSavedItem(name, mainBinding.llSave, mainBinding.hScrollView, LayoutInflater.from(this@MainActivity), mainBinding.etSearch)
                val intent = Intent(this@MainActivity, MapActivity::class.java).apply {
                    putExtra("name", name)
                    putExtra("address", address)
                    putExtra("latitude", latitude)
                    putExtra("longitude", longitude)
                }
                startActivity(intent)
            }
        })

        mainBinding.btnClose.setOnClickListener {
            mainBinding.etSearch.text?.clear()
        }

        searchSave.loadSavedItems(mainBinding.llSave, mainBinding.hScrollView, LayoutInflater.from(this), mainBinding.etSearch)
    }

    private fun showNoResults() {
        mainBinding.tvNoResult.visibility = View.VISIBLE
        adapter.updateProfiles(emptyList())
    }

    override fun onPause() {
        super.onPause()
        searchSave.saveSavedItems(mainBinding.llSave)
    }
}
