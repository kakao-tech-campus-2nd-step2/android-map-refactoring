package campus.tech.kakao.View.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.View.Adapter.HistoryAdapter
import campus.tech.kakao.View.Adapter.PlacesAdapter
import campus.tech.kakao.View.Activity.MainActivity
import campus.tech.kakao.ViewModel.SearchViewModel
import campus.tech.kakao.map.BuildConfig
import campus.tech.kakao.map.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {
    lateinit var searchView: SearchView
    lateinit var recyclerView: RecyclerView
    lateinit var noResultTextView: TextView
    lateinit var historyRecyclerView: RecyclerView
    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var adapter: PlacesAdapter

    private val viewModel: SearchViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews(view)
        setupRecyclerViews()
        setupSearchView()
        observeViewModel()
        viewModel.loadSelectedData()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadSelectedData()
    }

    private fun initializeViews(view: View) {
        searchView = view.findViewById(R.id.searchView2)
        recyclerView = view.findViewById(R.id.recyclerView)
        noResultTextView = view.findViewById(R.id.noResultTextView)
        historyRecyclerView = view.findViewById(R.id.historyRecyclerView)
    }

    private fun setupRecyclerViews() {
        recyclerView.layoutManager = LinearLayoutManager(context)
        historyRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        historyAdapter = HistoryAdapter(mutableListOf(), { id ->
            viewModel.deleteSelectedData(id)
        }, { historyItem -> searchView.setQuery(historyItem, true) })
        historyRecyclerView.adapter = historyAdapter

        adapter = PlacesAdapter(listOf()) { place ->
            viewModel.insertSelectedData(place.placeName)
            viewModel.selectPlace(place)
            navigateToMapFragment()
        }
        recyclerView.adapter = adapter
    }

    private fun setupSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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
        noResultTextView.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
        historyRecyclerView.visibility =
            if (historyAdapter.itemCount > 0) View.VISIBLE else View.GONE
    }

    private fun hideNoResultMessage() {
        noResultTextView.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
        historyRecyclerView.visibility =
            if (historyAdapter.itemCount > 0) View.VISIBLE else View.GONE
    }

    private fun observeViewModel() {
        viewModel.selectedData.observe(viewLifecycleOwner) { historyData ->
            historyAdapter.updateData(historyData)
            historyRecyclerView.visibility =
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