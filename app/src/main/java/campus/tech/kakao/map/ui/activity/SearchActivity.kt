package campus.tech.kakao.map.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.R
import campus.tech.kakao.map.data.db.SearchHistory
import campus.tech.kakao.map.repository.KakaoRepository
import campus.tech.kakao.map.ui.adapter.ResultRecyclerViewAdapter
import campus.tech.kakao.map.ui.adapter.SearchHistoryRecyclerViewAdapter
import campus.tech.kakao.map.ui.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {

    private lateinit var searchEditText: EditText
    private lateinit var resultRecyclerView: RecyclerView
    private lateinit var searchHistoryRecyclerView: RecyclerView
    private lateinit var noResults: TextView
    private lateinit var backButton: ImageButton
    private lateinit var resultRecyclerViewAdapter: ResultRecyclerViewAdapter
    private lateinit var searchHistoryRecyclerViewAdapter: SearchHistoryRecyclerViewAdapter

    private val searchViewModel: SearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        searchEditText = findViewById(R.id.search_edit_text)
        resultRecyclerView = findViewById(R.id.recycler_view)
        searchHistoryRecyclerView = findViewById(R.id.horizontal_recycler_view)
        noResults = findViewById(R.id.no_results)
        backButton = findViewById(R.id.back_button)

        initRecyclerViews()
        observeViewModel()

        searchEditText.addTextChangedListener { text ->
            text?.let { searchViewModel.searchPlaces(it.toString()) }
        }

        backButton.setOnClickListener {
            goBackToMap()
        }
    }

    private fun initRecyclerViews() {
        searchHistoryRecyclerViewAdapter = SearchHistoryRecyclerViewAdapter(
            searchHistory = mutableListOf(),
            onItemClick = { history ->
                searchEditText.setText(history.placeName)
                searchEditText.clearFocus()
                searchEditText.isFocusable = false
            },
            onItemDelete = { history ->
                searchViewModel.deleteSearchHistory(history)
            }
        )

        searchHistoryRecyclerView.adapter = searchHistoryRecyclerViewAdapter
        searchHistoryRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        resultRecyclerViewAdapter = ResultRecyclerViewAdapter(
            places = emptyList(),
            onItemClick = { place ->
                val searchHistory = SearchHistory(placeName = place.place_name)
                searchViewModel.addSearchHistory(searchHistory)
                searchViewModel.updateMapPosition(place)
                goBackToMap()
            }
        )

        resultRecyclerView.adapter = resultRecyclerViewAdapter
        resultRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun observeViewModel() {
        searchViewModel.searchResults.observe(this, Observer { places ->
            resultRecyclerViewAdapter.updatePlaces(places)
            showNoResultsMessage(places.isEmpty())
        })

        searchViewModel.searchHistory.observe(this, Observer { searchHistoryList ->
            searchHistoryRecyclerViewAdapter.updateSearchHistory(searchHistoryList)
        })
    }

    private fun showNoResultsMessage(show: Boolean) {
        if (show) {
            noResults.visibility = TextView.VISIBLE
            resultRecyclerView.visibility = RecyclerView.GONE
        } else {
            noResults.visibility = TextView.GONE
            resultRecyclerView.visibility = RecyclerView.VISIBLE
        }
    }

    private fun goBackToMap() {
        val searchToMapIntent = Intent(this, MapActivity::class.java)
        searchToMapIntent.putExtra("mapX", searchViewModel.mapX.value)
        searchToMapIntent.putExtra("mapY", searchViewModel.mapY.value)
        searchToMapIntent.putExtra("name", searchViewModel.name.value)
        searchToMapIntent.putExtra("address", searchViewModel.address.value)
        finish()
        startActivity(searchToMapIntent)
    }
}