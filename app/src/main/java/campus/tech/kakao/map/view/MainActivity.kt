package campus.tech.kakao.map.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import campus.tech.kakao.map.Adapter.LocationAdapter
import campus.tech.kakao.map.Adapter.SearchViewAdapter
import campus.tech.kakao.map.Model.LocationData
import campus.tech.kakao.map.databinding.ActivityMainBinding
import campus.tech.kakao.map.viewmodel.MainViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var locationAdapter: LocationAdapter
    private lateinit var searchAdapter: SearchViewAdapter
    private var searchList = ArrayList<LocationData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initialize()
        setupRecyclerViews()
        setupSearchView()
        observeUiState()
        setCancelBtn()
    }

    private fun initialize() {
        loadSearchList()
    }

    private fun setupRecyclerViews() {
        locationAdapter = LocationAdapter { location ->
            handleLocationClick(location)
        }
        binding.recyclerView.apply {
            adapter = locationAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        searchAdapter = SearchViewAdapter(searchList) { removedItem ->
            handleSearchItemRemoval(removedItem)
        }
        binding.searchView.apply {
            adapter = searchAdapter
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setupSearchView() {
        binding.inputText.addTextChangedListener { text ->
            viewModel.searchLocations(text.toString())
        }
    }

    private fun observeUiState() {
        viewModel.uiState.observe(this) { uiState ->
            locationAdapter.submitList(uiState.locationList)
            binding.resultView.isVisible = uiState.isShowText
        }
    }

    private fun setCancelBtn() {
        binding.cancelBtn.setOnClickListener {
            binding.inputText.text.clear()
        }
    }

    private fun handleLocationClick(location: LocationData) {
        Log.d("MainActivity", "Location item clicked: ${location.name}")
        addToSearchList(location)
        val intent = Intent(this, MapActivity::class.java)
        intent.putExtra("selectedLocation", Gson().toJson(location))
        startActivity(intent)
        finish()
    }

    private fun handleSearchItemRemoval(removedItem: LocationData) {
        val index = searchList.indexOf(removedItem)
        if (index != -1) {
            searchList.removeAt(index)
            searchAdapter.notifyItemRemoved(index)
            if (searchList.isEmpty()) {
                binding.searchView.isVisible = false
            }
            saveSearchList()
        }
    }

    private fun addToSearchList(locationData: LocationData) {
        if (!searchList.contains(locationData)) {
            searchList.add(0, locationData)
            searchAdapter.notifyItemInserted(0)
        } else {
            val index = searchList.indexOf(locationData)
            if (index > 0) {
                searchList.removeAt(index)
                searchList.add(0, locationData)
                searchAdapter.notifyItemMoved(index, 0)
            }
        }
        binding.searchView.isVisible = true
        binding.searchView.scrollToPosition(0)
        saveSearchList()
        Log.d("MainActivity", "Item added to search list and saved. Size: ${searchList.size}")
    }

    private fun saveSearchList() {
        val sharedPref = getPreferences(MODE_PRIVATE)
        val editor = sharedPref.edit()
        val jsonString = Gson().toJson(searchList)
        editor.putString("search_list", jsonString)
        editor.apply()
        Log.d("MainActivity", "Search list saved. JSON: $jsonString")
    }

    private fun loadSearchList() {
        val sharedPref = getPreferences(MODE_PRIVATE)
        val jsonString = sharedPref.getString("search_list", null)
        if (jsonString != null) {
            val type = object : TypeToken<ArrayList<LocationData>>() {}.type
            searchList = Gson().fromJson(jsonString, type)
        }
        Log.d("searchList", "$searchList")

        binding.searchView.isVisible = searchList.isNotEmpty()
        Log.d("MainActivity", "SearchView visibility set, items: ${searchList.size}")
    }

    override fun onDestroy() {
        super.onDestroy()
        // Any cleanup if needed
    }
}