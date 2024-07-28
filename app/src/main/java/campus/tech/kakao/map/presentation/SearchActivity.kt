package campus.tech.kakao.map.presentation

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.ActivityMainBinding
import campus.tech.kakao.map.presentation.adapter.SearchedPlaceAdapter
import campus.tech.kakao.map.presentation.adapter.LogAdapter
import campus.tech.kakao.map.domain.model.Place
import campus.tech.kakao.map.util.PlaceMapper
import kotlinx.coroutines.launch

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var searchedPlaceAdapter: SearchedPlaceAdapter
    private lateinit var logAdapter: LogAdapter
    private lateinit var viewModel: SearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    private fun init() {
        initViewModel()
        initBinding()
        setupRecyclerViews()
        observeViewModel()
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this,SearchViewModel.Factory)
            .get(SearchViewModel::class.java)
    }

    private fun initBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }

    private fun setupRecyclerViews() {
        setupSearchedPlaceRecyclerView()
        setupLogRecyclerView()
    }

    private fun setupSearchedPlaceRecyclerView() {
        val searchedPlaceRecyclerView = binding.recyclerPlace
        searchedPlaceAdapter = SearchedPlaceAdapter { place ->
            viewModel.updateLogs(place)
            handlePlaceClick(place)
        }

        searchedPlaceRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@SearchActivity)
            addItemDecoration(DividerItemDecoration(this@SearchActivity, DividerItemDecoration.VERTICAL ))
            adapter = searchedPlaceAdapter
        }
    }

    private fun handlePlaceClick(place: Place) {
        val intent = Intent(this, MapActivity::class.java).apply {
            putExtra("placeData", viewModel.getPlaceById(place.id))
        }
        setResult(RESULT_OK,intent)
        finish()
    }

    private fun setupLogRecyclerView() {
        val logRecyclerView = binding.recyclerLog
        logAdapter = LogAdapter { id -> viewModel.removeLog(id) }
        logAdapter.submitList(viewModel.getLogs())

        logRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@SearchActivity, RecyclerView.HORIZONTAL, false)
            adapter = logAdapter
        }
    }

    private fun observeViewModel() {

        lifecycleScope.launch {
            viewModel.searchedPlaces.collect { places ->
                updateSearchedPlaceList(places)
                binding.tvHelpMessage.visibility = if (places.isEmpty()) View.VISIBLE else View.GONE
            }
        }
        viewModel.logList.observe(this, Observer { logList ->
            logAdapter.submitList(PlaceMapper.mapPlaces(logList))
        })
    }

    private fun updateSearchedPlaceList(places: List<Place>) {
        searchedPlaceAdapter.submitList(PlaceMapper.mapPlaces(places))
    }
}
