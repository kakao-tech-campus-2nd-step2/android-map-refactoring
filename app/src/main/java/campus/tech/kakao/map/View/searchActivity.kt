package campus.tech.kakao.map.View

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import campus.tech.kakao.map.R
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import campus.tech.kakao.map.Adapter.PlaceAdapter
import campus.tech.kakao.map.Model.SearchViewModel
import campus.tech.kakao.map.databinding.ActivitySearchBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class searchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private val searchViewModel: SearchViewModel by viewModels()
    private lateinit var placeAdapter: PlaceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search)
        binding.viewModel = searchViewModel
        binding.lifecycleOwner = this

        initRecyclerViews()
        observeSearchResults()
        observeSavedSearches()
    }

    private fun initRecyclerViews() {
        placeAdapter = PlaceAdapter()
        binding.RecyclerVer.adapter = placeAdapter
        binding.RecyclerVer.layoutManager = LinearLayoutManager(this)

        binding.recyclerHor.adapter = searchViewModel.savedSearchAdapter
        binding.recyclerHor.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun observeSearchResults() {
        searchViewModel.searchResults.observe(this, Observer { results ->
            placeAdapter.submitList(results)
        })
    }

    private fun observeSavedSearches() {
        searchViewModel.savedSearches.observe(this, Observer { savedSearches ->
            searchViewModel.savedSearchAdapter.updateData(savedSearches.map { it.place_name }.toString())
        })
    }

}









