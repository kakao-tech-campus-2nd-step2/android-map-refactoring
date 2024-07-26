package campus.tech.kakao.map.presentation

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.databinding.ActivityPlaceBinding
import campus.tech.kakao.map.domain.dto.PlaceVO
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlaceActivity : AppCompatActivity() {
    private val placeViewModel: PlaceViewModel by viewModels()
    private lateinit var placeAdapter: PlaceAdapter
    private lateinit var historyAdapter: SearchHistoryAdapter

    private lateinit var binding: ActivityPlaceBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlaceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeAdapters()
        initializeRecyclerView()

        setUpSearchEditText()
        setUpRemoveButton()

        observeViewModel()
    }

    private fun showEmptyMessage() {
        binding.emptyMessage.visibility = TextView.VISIBLE
        binding.placeRecyclerView.visibility = RecyclerView.GONE
    }

    private fun clearSearchEditText() {
        binding.searchEditText.text.clear()
    }

    private fun showRecyclerView(places: List<PlaceVO>) {
        binding.emptyMessage.visibility = TextView.GONE
        binding.placeRecyclerView.visibility = RecyclerView.VISIBLE
        placeAdapter.updateData(places)
    }

    private fun updateUI(places: List<PlaceVO>?) {
        if (places.isNullOrEmpty()) {
            showEmptyMessage()
        } else {
            showRecyclerView(places!!)
        }
    }

    private fun observeViewModel() {
        placeViewModel.places.observe(this) { places ->
            updateUI(places)

        }

        placeViewModel.searchHistory.observe(this) { history ->
            historyAdapter.updateData(history)
        }

        placeViewModel.loadSearchHistory()
    }

    private fun initializeRecyclerView() {
        binding.placeRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.placeRecyclerView.adapter = placeAdapter

        binding.historyRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.historyRecyclerView.adapter = historyAdapter
    }

    private fun initializeAdapters() {
        Log.d("testt","initializeAdapters")
        placeAdapter = PlaceAdapter { place ->
            Log.d("testt", "place $place")
            placeViewModel.saveSearchQuery(place)
            sendPositiontoMap(place)
        }

        historyAdapter = SearchHistoryAdapter(
            historyList = mutableListOf(),
            onDeleteClick = { query ->
                placeViewModel.removeSearchQuery(query)
            },
            onItemClick = { query ->
                Log.d("testt", "search $query")
                binding.searchEditText.setText(query)
                placeViewModel.searchPlaces(query)
            }
        )
    }
    private fun sendPositiontoMap(place: PlaceVO) {
        val latlng = placeViewModel.getPlaceLocation(place)
        val intent = Intent(this, MapActivity::class.java).apply {
            putExtra("place", place)
        }
        startActivity(intent)
    }

    private fun setUpSearchEditText() {
        binding.searchEditText.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //no-op}
                }

                override fun onTextChanged(
                    p0: CharSequence?, p1: Int, p2: Int, p3: Int,
                ) {
                    if (p0.isNullOrBlank()) {
                        showEmptyMessage()
                        return
                    }
                    placeViewModel.searchPlaces(query = p0.toString())
                }

                override fun afterTextChanged(p0: Editable?) {
                //no-op
                }
            },
        )
    }

    private fun setUpRemoveButton() {
        binding.cancelButton.setOnClickListener {
            clearSearchEditText()
            showEmptyMessage()
        }
    }
}
