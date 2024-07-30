package campus.tech.kakao.map.presentation.view

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.databinding.ActivityPlaceBinding
import campus.tech.kakao.map.domain.dto.PlaceVO
import campus.tech.kakao.map.presentation.viewmodel.PlaceViewModel
import campus.tech.kakao.map.presentation.adapter.PlaceAdapter
import campus.tech.kakao.map.presentation.adapter.SearchHistoryAdapter
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
            showRecyclerView(places)
        }
    }

    private fun observeViewModel() {
        placeViewModel.places.observe(this) { places ->
            updateUI(places)

        }

        placeViewModel.searchHistory.observe(this) { history ->
            historyAdapter.updateData(history)
        }

        placeViewModel.navigateToMap.observe(this) { place ->
            place?.let {
                sendPositiontoMap(it)
                placeViewModel.doneNavigating()
            }
        }

        placeViewModel.loadSearchHistory()
    }

    private fun initializeAdapters() {
        // 어댑터 초기화
        placeAdapter = PlaceAdapter(viewModel = placeViewModel)
        historyAdapter = SearchHistoryAdapter(viewModel = placeViewModel)

        // RecyclerView에 어댑터 설정
        binding.placeRecyclerView.adapter = placeAdapter
        binding.historyRecyclerView.adapter = historyAdapter
    }
    private fun sendPositiontoMap(place: PlaceVO) {
        placeViewModel.getPlaceLocation(place)
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
