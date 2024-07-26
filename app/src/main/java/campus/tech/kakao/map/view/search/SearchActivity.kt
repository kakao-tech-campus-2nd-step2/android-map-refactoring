package campus.tech.kakao.map.view.search

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import campus.tech.kakao.map.adapter.keyword.KeywordAdapter
import campus.tech.kakao.map.adapter.search.SearchAdapter
import campus.tech.kakao.map.databinding.ActivitySearchBinding
import campus.tech.kakao.map.viewmodel.DualViewModelClickListener
import campus.tech.kakao.map.viewmodel.keyword.KeywordViewModel
import campus.tech.kakao.map.viewmodel.keyword.KeywordViewModelFactory
import campus.tech.kakao.map.viewmodel.search.SearchViewModel
import campus.tech.kakao.map.viewmodel.search.SearchViewModelFactory

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private lateinit var searchViewModel: SearchViewModel
    private lateinit var keywordViewModel: KeywordViewModel
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var keywordAdapter: KeywordAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViewModels()
        setupBinding()
        setupSearchResultViewRecyclerView()
        setupKeywordHistoryView()
        setupEditText()
        observeSearchResult()
        observeKeywordHistory()
        observeLocalInformation()
    }

    override fun onDestroy() {
        super.onDestroy()
        keywordViewModel.close()
    }

    private fun setupViewModels() {
        searchViewModel =
            ViewModelProvider(this, SearchViewModelFactory(this))[SearchViewModel::class.java]
        keywordViewModel =
            ViewModelProvider(this, KeywordViewModelFactory(this))[KeywordViewModel::class.java]

    }

    private fun setupBinding() {
        binding = ActivitySearchBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    private fun setupSearchResultViewRecyclerView() {
        searchAdapter = SearchAdapter(DualViewModelClickListener(keywordViewModel, searchViewModel))
        binding.searchResultView.apply {
            layoutManager = LinearLayoutManager(this@SearchActivity)
            adapter = searchAdapter
        }

        val dividerItemDecoration = DividerItemDecoration(
            binding.searchResultView.context,
            (binding.searchResultView.layoutManager as LinearLayoutManager).orientation
        )
        binding.searchResultView.addItemDecoration(dividerItemDecoration)
    }

    private fun setupKeywordHistoryView() {
        keywordAdapter = KeywordAdapter(keywordViewModel)
        binding.keywordHistoryView.apply {
            layoutManager =
                LinearLayoutManager(this@SearchActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = keywordAdapter
        }
        keywordViewModel.readKeywordHistory()
    }

    private fun setupEditText() {
        val handler = Handler(Looper.getMainLooper())
        var runnable: Runnable? = null
        binding.etSearchTextInput.setOnFocusChangeListener { _, _ ->
            keywordViewModel.readKeywordHistory()
        }

        binding.etSearchTextInput.doAfterTextChanged {
            runnable?.let { handler.removeCallbacks(it) }

            runnable = Runnable {
                binding.etSearchTextInput.text.toString().let {
                    searchViewModel.searchLocationData(it)
                }
            }

            handler.postDelayed(runnable!!, 500)
        }

        binding.ivDeleteTextInput.setOnClickListener {
            binding.etSearchTextInput.setText("")
        }
    }

    private fun observeSearchResult() {
        searchViewModel.items.observe(this) {
            if (it.isEmpty()) {
                binding.searchResultView.visibility = View.GONE
                binding.tvEmpty.visibility = View.VISIBLE
            } else {
                searchAdapter.submitList(it)
                binding.searchResultView.visibility = View.VISIBLE
                binding.tvEmpty.visibility = View.GONE
            }
        }
    }

    private fun observeKeywordHistory() {
        keywordViewModel.keyword.observe(this) {
            keywordAdapter.submitList(it)
        }

        keywordViewModel.keywordClicked.observe(this) {
            binding.etSearchTextInput.setText(it)
        }
    }

    private fun observeLocalInformation() {
        searchViewModel.localInformation.observe(this) {
            val intent = Intent().apply {
                putExtra("selected_location", it)
            }
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}

