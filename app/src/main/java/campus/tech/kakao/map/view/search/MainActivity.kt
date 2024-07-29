package campus.tech.kakao.map.view.search

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import campus.tech.kakao.map.databinding.ActivityMainBinding
import campus.tech.kakao.map.model.Location
import campus.tech.kakao.map.model.SavedLocation
import campus.tech.kakao.map.view.map.MapActivity
import campus.tech.kakao.map.viewmodel.LocationViewModel
import campus.tech.kakao.map.viewmodel.SavedLocationViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), OnItemSelectedListener, OnSearchClickListener {
    private val locationViewModel: LocationViewModel by viewModels()
    private val savedLocationViewModel: SavedLocationViewModel by viewModels()

    private lateinit var locationAdapter: LocationAdapter
    private lateinit var savedLocationAdapter: SavedLocationAdapter
    private lateinit var activityMainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        locationAdapter = LocationAdapter(this)
        savedLocationAdapter = SavedLocationAdapter(this)

        setupSearchEditText()
        setupViewModels()
        setupRecyclerViews()
    }

    private fun setupSearchEditText() {
        activityMainBinding.searchEditTextMain.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString()
                if(query.isEmpty()) return
                locationViewModel.searchLocationsFromKakaoAPI(query) { searchLocationsSize ->
                    handleNoResultMessage(searchLocationsSize)
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })
        activityMainBinding.searchEditTextMain.requestFocus()
        activityMainBinding.searchClickListener = this
    }

    private fun setupViewModels() {
        savedLocationViewModel.setSavedLocation()
        observeSavedLocationViewModel()

        locationViewModel.setLocationsFromKakaoAPI()
        observeLocationsViewModel()
    }

    private fun observeSavedLocationViewModel() {
        savedLocationViewModel.savedLocation.observe(this, Observer {
            savedLocationAdapter.submitList(it?.toList() ?: emptyList())
            if (it.size > 0) {
                activityMainBinding.savedLocationRecyclerView.visibility = View.VISIBLE
            } else {
                activityMainBinding.savedLocationRecyclerView.visibility = View.GONE
            }
        })
    }

    private fun observeLocationsViewModel() {
        locationViewModel.searchedLocations.observe(this, Observer {
            locationAdapter.submitList(it?.toList() ?: emptyList())
        })
    }

    private fun setupRecyclerViews() {
        activityMainBinding.locationRecyclerView.layoutManager = LinearLayoutManager(this)
        activityMainBinding.locationRecyclerView.adapter = locationAdapter

        activityMainBinding.savedLocationRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        activityMainBinding.savedLocationRecyclerView.adapter = savedLocationAdapter
    }

    override fun onLocationViewClicked(location: Location) {
        savedLocationViewModel.addSavedLocation(location.id, location.title)

        val intent = Intent(this@MainActivity, MapActivity::class.java)
        intent.putExtra("location", location)
        startActivity(intent)
    }

    override fun onSavedLocationClearButtonClicked(savedLocation: SavedLocation) {
        savedLocationViewModel.deleteSavedLocation(savedLocation)
    }

    override fun onSavedLocationViewClicked(title: String) {
        updateEditText(title)
        locationViewModel.searchLocationsFromKakaoAPI(title){ searchLocationsSize ->
            handleNoResultMessage(searchLocationsSize)
        }
    }

    override fun onSearchClearButtonClicked() {
        activityMainBinding.searchEditTextMain.setText("")
    }

    private fun updateEditText(title: String) {
        activityMainBinding.searchEditTextMain.setText(title)
        activityMainBinding.searchEditTextMain.setSelection(activityMainBinding.searchEditTextMain.text.length)
    }

    private fun handleNoResultMessage(searchLocationsSize: Int) {
        if (searchLocationsSize > 0) {
            activityMainBinding.noResultTextView.visibility = View.GONE
        } else {
            activityMainBinding.noResultTextView.visibility = View.VISIBLE
        }
    }
}
