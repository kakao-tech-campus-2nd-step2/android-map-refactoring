package campus.tech.kakao.map.presentation

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.DatabaseListener
import campus.tech.kakao.map.domain.model.Location
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.ActivitySearchBinding
import campus.tech.kakao.map.domain.model.History
import campus.tech.kakao.map.presentation.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchActivity : AppCompatActivity(), DatabaseListener {

    private val viewModel: SearchViewModel by viewModels()
    private lateinit var binding: ActivitySearchBinding
    private lateinit var searchResultAdapter: ResultRecyclerAdapter
    private lateinit var searchHistoryAdapter: HistoryRecyclerAdapter
    private val searchBoxWatcher = getSearchBoxWatcher()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search)
        binding.main = this

        binding.searchBox.addTextChangedListener(searchBoxWatcher)

        binding.clear.setOnClickListener {
            binding.searchBox.text.clear()
        }

        initSearchResultView()
        initSearchHistoryView()
        observeData()
    }

    override fun deleteHistory(oldHistory: History) {
        viewModel.deleteHistory(oldHistory)
    }

    override fun insertHistory(newHistory: History) {
        viewModel.insertHistory(newHistory)
    }

    override fun showMap() {
        finish()
    }

    override fun insertLastLocation(location: Location) {
        viewModel.insertLastLocation(location)
    }

    override fun searchHistory(locName: String, isExactMatch: Boolean) {
        binding.searchBox.removeTextChangedListener(searchBoxWatcher)
        binding.searchBox.setText(locName)
        binding.searchBox.addTextChangedListener(searchBoxWatcher)
        viewModel.searchKeyword(locName)
    }

    private fun search(locName: String) {
        viewModel.searchKeyword(locName)
    }

    private fun hideResult() {
        binding.searchResult.isVisible = false
        binding.message.isVisible = true
    }
    private fun showResult() {
        binding.searchResult.isVisible = true
        binding. message.isVisible = false
    }

    private fun initSearchResultView() {
        searchResultAdapter =
            ResultRecyclerAdapter(viewModel.getAllResult(), layoutInflater, this)
        binding.searchResult.adapter = searchResultAdapter
        binding.searchResult.layoutManager =
            LinearLayoutManager(this@SearchActivity, LinearLayoutManager.VERTICAL, false)
    }

    private fun initSearchHistoryView() {
        searchHistoryAdapter =
            HistoryRecyclerAdapter(viewModel.getAllHistory(), layoutInflater, this)
        binding.searchHistory.adapter = searchHistoryAdapter
        binding.searchHistory.layoutManager =
            LinearLayoutManager(this@SearchActivity, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun observeData() {
        viewModel.searchHistory.observe(this, Observer {
            searchHistoryAdapter.history = it
            searchHistoryAdapter.refreshList()
        })
        viewModel.searchResult.observe(this, Observer {
            searchResultAdapter.searchResult = it
            if (it.isNotEmpty() && binding.searchBox.text.isNotEmpty()) {
                showResult()
            } else {
                hideResult()
            }
            searchResultAdapter.refreshList()
        })
    }

    private fun getSearchBoxWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // TODO: 필요시 구현 예정
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // TODO: 필요시 구현 예정
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    hideResult()
                } else {
                    search(s.toString())
                }
            }
        }
    }
}
