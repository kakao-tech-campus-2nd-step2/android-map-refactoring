package campus.tech.kakao.map

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import campus.tech.kakao.map.databinding.ActivitySearchBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import javax.inject.Inject
@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {
    @Inject lateinit var preferenceManager: PreferenceManager
    private val viewModel: SearchViewModel by viewModels()

    private val placeAdapter: PlaceAdapter by lazy {
        PlaceAdapter(
            emptyList(),
            object : PlaceAdapter.OnItemClickListener {
                override fun onItemClick(position: Int) {
                    val item = placeAdapter.getItem(position)
                    val searchHistory = SearchHistory(0, item.placeName, item.x, item.y)
                    viewModel.insert(searchHistory)

                    val intent = Intent(this@SearchActivity, MainActivity::class.java).apply {
                        putExtra("longitude", item.x)
                        putExtra("latitude", item.y)
                        putExtra("name", item.placeName)
                        putExtra("address", item.addressName)
                        flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                    }
                    startActivity(intent)
                }
            }
        )
    }

    private val historyAdapter: HistoryAdapter by lazy {
        HistoryAdapter(
            emptyList(),
            object : HistoryAdapter.OnItemClickListener {
                override fun onItemClick(position: Int) {
                    val item = viewModel.searchHistoryList.value?.get(position)
                    if (item != null) {
                        searchViewBinding.search.setText(item.searchHistory)
                    }
                }
                override fun onXMarkClick(position: Int) {
                    val item = viewModel.searchHistoryList.value?.get(position)
                    if (item != null) {
                        viewModel.delete(item)
                    }
                }
            }
        )
    }

    private lateinit var searchViewBinding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchViewBinding = DataBindingUtil.setContentView(this, R.layout.activity_search)
        searchViewBinding.viewModel = viewModel
        searchViewBinding.lifecycleOwner = this

        setupRecyclerViews(searchViewBinding)
        setupSearchEditText(searchViewBinding)
        observeViewModel(searchViewBinding)
    }

    private fun setupRecyclerViews(searchBinding: ActivitySearchBinding) {
        searchBinding.placeResult.apply {
            layoutManager = LinearLayoutManager(this@SearchActivity, LinearLayoutManager.VERTICAL, false)
            adapter = placeAdapter
        }

        searchBinding.searchHistory.apply {
            layoutManager = LinearLayoutManager(this@SearchActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = historyAdapter
        }
    }

    private fun setupSearchEditText(searchBinding: ActivitySearchBinding) {
        val searchEditText = searchBinding.search
        val timeMillis = 300L
        val debounce = debounce<String>(timeMillis, CoroutineScope(Dispatchers.Main)) { query ->
            viewModel.getPlace(query) }

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val searchText = s.toString().trim()
                debounce(searchText)
            }
        })
    }

    private fun observeViewModel(searchBinding: ActivitySearchBinding) {
        viewModel.searchHistoryList.observe(this@SearchActivity, Observer {
            historyAdapter.setData(it)
            Log.d("insert", "아이템 변경됨!")
        })
        viewModel.getAllSearchHistory()

        viewModel.locationList.observe(this@SearchActivity, Observer {
            placeAdapter.setData(it)
        })
    }

    private fun <T> debounce(
        timeMillis: Long = 500L,
        coroutineScope: CoroutineScope,
        block: (T) -> Unit
    ): (T) -> Unit {
        var debounceJob: Job? = null
        return {
            debounceJob?.cancel()
            debounceJob = coroutineScope.launch {
                delay(timeMillis)
                block(it)
            }
        }
    }
}
