package campus.tech.kakao.map.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.databinding.ActivityMainBinding
import android.util.Log
import android.content.Intent
import android.view.View
import campus.tech.kakao.map.adapter.SavedSearchAdapter
import campus.tech.kakao.map.adapter.SearchAdapter
import campus.tech.kakao.map.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    private lateinit var searchAdapter: SearchAdapter
    private lateinit var savedSearchAdapter: SavedSearchAdapter

    companion object {
        const val EXTRA_PLACE_NAME = "place_name"
        const val EXTRA_PLACE_ADDRESS = "place_address"
        const val EXTRA_PLACE_X = "place_x"
        const val EXTRA_PLACE_Y = "place_y"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", "onCreate called")

        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        searchAdapter = SearchAdapter { result ->
            viewModel.addSearch(result.name, result.address, result.category, result.x, result.y)
            val intent = Intent(this, MapActivity::class.java).apply {
                putExtra(EXTRA_PLACE_NAME, result.name)
                putExtra(EXTRA_PLACE_ADDRESS, result.address)
                putExtra(EXTRA_PLACE_X, result.x)
                putExtra(EXTRA_PLACE_Y, result.y)
            }
            startActivity(intent)
        }

        savedSearchAdapter = SavedSearchAdapter (
            onSearchClicked = {viewModel.searchSavedPlace(it.name)},
            onDeleteClicked = {place -> viewModel.removeSearch(place.name, place.address, place.category)}
        )

        binding.searchAdapter = searchAdapter
        binding.savedSearchAdapter = savedSearchAdapter

        setContentView(binding.root)
        Log.d("MainActivity", "View binding initialized")

        setupRecyclerViews()

        viewModel.uiState.observe(this, Observer { uiState ->
            Log.d("MainActivity", "UI state updated: $uiState")
            searchAdapter.updateResults(uiState.searchResults)
            savedSearchAdapter.updateSearches(uiState.savedSearches)
            binding.noResult.visibility = if(uiState.noResultsVisible) View.VISIBLE else View.GONE
            binding.searchRecyclerView.visibility = if(uiState.searchRecyclerViewVisible) View.VISIBLE else View.GONE
            binding.savedSearchRecyclerView.visibility = if(uiState.savedSearchRecyclerViewVisible) View.VISIBLE else View.GONE
        })

        /*
        viewModel.searchResults.observe(this, Observer { results ->
            Log.d("MainActivity", "Search results updated: $results")
            searchAdapter.updateResults(results)
        })

        viewModel.savedSearches.observe(this, Observer { searches ->
            Log.d("MainActivity", "Saved searches updated: $searches")
            savedSearchAdapter.updateSearches(searches)
        })

        viewModel.noResultsVisible.observe(this, Observer { visible ->
            binding.noResult.visibility = if(visible) View.VISIBLE else View.GONE
        })

        viewModel.searchRecyclerViewVisibility.observe(this, Observer { visible ->
            binding.searchRecyclerView.visibility = if (visible) View.VISIBLE else View.GONE
        })

        viewModel.savedSearchRecyclerViewVisibility.observe(this, Observer { visible ->
            binding.savedSearchRecyclerView.visibility = if (visible) View.VISIBLE else View.GONE
        })*/

        binding.inputSearch.setOnFocusChangeListener { _, hasFocus ->
            if(hasFocus && binding.inputSearch.text.isEmpty()) {
                viewModel.setSavedSearchRecyclerViewVisibility(true)
            }
        }

        binding.inputSearch.addTextChangedListener ( object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString()
                Log.d("MainActivity", "Search query: $query")
                if(query.isNotEmpty()) {
                    viewModel.searchPlaces(query)
                } else {
                    viewModel.clearSearchResults()
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

    }

    private fun setupRecyclerViews() {

        binding.searchRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.searchRecyclerView.adapter = searchAdapter

        binding.savedSearchRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding.savedSearchRecyclerView.adapter = savedSearchAdapter

        Log.d("MainActivity", "RecyclerViews set up")
    }

}
