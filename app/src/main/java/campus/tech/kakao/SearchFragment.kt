package campus.tech.kakao.View


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
import campus.tech.kakao.map.R
import campus.tech.kakao.Model.ResultSearchKeyword
import campus.tech.kakao.Model.RetrofitClient
import campus.tech.kakao.Model.RoomDb
import campus.tech.kakao.ViewModel.SearchViewModel
import campus.tech.kakao.ViewModel.SearchViewModelFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchFragment : Fragment() {
    lateinit var searchView: SearchView
    lateinit var recyclerView: RecyclerView
    lateinit var noResultTextView: TextView
    lateinit var historyRecyclerView: RecyclerView
    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var adapter: PlacesAdapter

    private val viewModel: SearchViewModel by viewModels {
        SearchViewModelFactory(RoomDb(requireContext()))
    }

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
        historyRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        historyAdapter = HistoryAdapter(mutableListOf(), { id ->
            viewModel.deleteSelectedData(id)
        }, { historyItem -> searchView.setQuery(historyItem, true) })
        historyRecyclerView.adapter = historyAdapter

        adapter = PlacesAdapter(listOf()) { name ->
            viewModel.insertSelectedData(name)
        }
        recyclerView.adapter = adapter
    }

    private fun setupSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    searchPlaces(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    showNoResultMessage()
                    adapter.updateData(emptyList())
                } else {
                    searchPlaces(newText)
                }
                return true
            }
        })
    }

    private fun searchPlaces(query: String) {
        val apiKey = "KakaoAK ${campus.tech.kakao.map.BuildConfig.KAKAO_REST_API_KEY}"

        RetrofitClient.instance.searchPlaces(apiKey, query).enqueue(object : Callback<ResultSearchKeyword> {
            override fun onResponse(call: Call<ResultSearchKeyword>, response: Response<ResultSearchKeyword>) {
                if (response.isSuccessful && response.body() != null) {
                    val places = response.body()?.documents ?: emptyList()
                    if (places.isEmpty()) {
                        showNoResultMessage()
                    } else {
                        hideNoResultMessage()
                        adapter.updateData(places)
                    }
                } else {
                    showNoResultMessage()
                }
            }

            override fun onFailure(call: Call<ResultSearchKeyword>, t: Throwable) {
                showNoResultMessage()
            }
        })
    }

    private fun showNoResultMessage() {
        noResultTextView.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
        historyRecyclerView.visibility = if (historyAdapter.itemCount > 0) View.VISIBLE else View.GONE
    }

    private fun hideNoResultMessage() {
        noResultTextView.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
        historyRecyclerView.visibility = if (historyAdapter.itemCount > 0) View.VISIBLE else View.GONE
    }

    private fun observeViewModel() {
        viewModel.selectedData.observe(viewLifecycleOwner) { historyData ->
            historyAdapter.updateData(historyData)
            historyRecyclerView.visibility = if (historyData.isNotEmpty()) View.VISIBLE else View.GONE
        }
    }
}