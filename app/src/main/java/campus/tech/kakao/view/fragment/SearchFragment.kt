package campus.tech.kakao.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import campus.tech.kakao.view.adapter.HistoryAdapter
import campus.tech.kakao.view.adapter.PlacesAdapter
import campus.tech.kakao.view.activity.MainActivity
import campus.tech.kakao.viewmodel.SearchViewModel
import campus.tech.kakao.map.BuildConfig
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private val viewModel: SearchViewModel by viewModels()
    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var adapter: PlacesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false).apply {
            viewModel = this@SearchFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerViews()
        setupSearchView()
        observeViewModel()
        viewModel.loadSelectedData()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadSelectedData()
    }

    private fun setupRecyclerViews() {
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.historyRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        historyAdapter = HistoryAdapter(mutableListOf(), { id ->
            viewModel.deleteSelectedData(id)
        }, { historyItem -> binding.searchView2.setQuery(historyItem, true) })
        binding.historyRecyclerView.adapter = historyAdapter

        adapter = PlacesAdapter(listOf()) { place ->
            viewModel.insertSelectedData(place.placeName)
            viewModel.selectPlace(place)
            navigateToMapFragment()
        }
        binding.recyclerView.adapter = adapter
    }

    private fun setupSearchView() {
        binding.searchView2.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    viewModel.searchPlaces(BuildConfig.KAKAO_REST_API_KEY, query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    showNoResultMessage()
                    adapter.updateData(emptyList())
                } else {
                    viewModel.searchPlaces(BuildConfig.KAKAO_REST_API_KEY, newText)
                }
                return true
            }
        })
    }

    private fun showNoResultMessage() {
        binding.noResultTextView.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.GONE
        binding.historyRecyclerView.visibility =
            if (historyAdapter.itemCount > 0) View.VISIBLE else View.GONE
    }

    private fun hideNoResultMessage() {
        binding.noResultTextView.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE
        binding.historyRecyclerView.visibility =
            if (historyAdapter.itemCount > 0) View.VISIBLE else View.GONE
    }

    private fun observeViewModel() {
        viewModel.selectedData.observe(viewLifecycleOwner) { historyData ->
            historyAdapter.updateData(historyData)
            binding.historyRecyclerView.visibility =
                if (historyData.isNotEmpty()) View.VISIBLE else View.GONE
        }

        viewModel.searchResults.observe(viewLifecycleOwner) { searchResults ->
            if (searchResults.isEmpty()) {
                showNoResultMessage()
            } else {
                hideNoResultMessage()
                adapter.updateData(searchResults)
            }
        }
    }

    private fun navigateToMapFragment() {
        viewModel.place.value?.let { place ->
            (activity as MainActivity).clearBackStack()
            val fragment = MapFragment().apply {
                arguments = Bundle().apply {
                    putDouble("x", place.x!!)
                    putDouble("y", place.y!!)
                    putString("placeName", place.placeName)
                    putString("roadAddressName", place.roadAddressName)
                }
            }
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit()
        }
    }
}