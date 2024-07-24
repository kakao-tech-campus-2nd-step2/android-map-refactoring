package ksc.campus.tech.kakao.map.presentation.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kakao.vectormap.KakaoMapSdk
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ksc.campus.tech.kakao.map.R
import ksc.campus.tech.kakao.map.presentation.viewmodels.SearchActivityViewModel
import ksc.campus.tech.kakao.map.presentation.views.adapters.SearchKeywordAdapter
import ksc.campus.tech.kakao.map.presentation.views.adapters.SearchKeywordClickCallback
import ksc.campus.tech.kakao.map.presentation.views.fragments.KakaoMapFragment
import ksc.campus.tech.kakao.map.presentation.views.fragments.SearchResultFragment

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var locationInfoNameView: TextView
    private lateinit var locationInfoAddressView: TextView

    private val fragmentManager = supportFragmentManager
    private lateinit var searchFragment: Fragment
    private lateinit var mapFragment: Fragment

    private lateinit var searchResultFragmentContainer: FragmentContainerView
    private lateinit var searchInput: SearchView
    private lateinit var keywordRecyclerView: RecyclerView

    val searchViewModel: SearchActivityViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initiateFragments()
        initiateViews()

        setInitialValueToAdapter()
        initiateLiveDataObservation()

        checkKakaoSdk()
    }

    private fun initiateLiveDataObservation() {
        searchViewModel.searchText.observe(this) {
            searchInput.setQuery(it, false)
        }
        searchViewModel.activeContent.observe(this) {
            if (it == SearchActivityViewModel.ContentType.MAP) {
                switchToMapMenu()
            } else {
                switchToSearchMenu()
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                searchViewModel.selectedLocation.collect {
                    updateText(it?.name ?: "", it?.address ?: "")

                }
            }

        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                searchViewModel.keywords.collectLatest {
                    (keywordRecyclerView.adapter as? SearchKeywordAdapter)?.submitList(it.asReversed())
                    setKeywordRecyclerViewActive(it.isNotEmpty())
                }
            }
        }
    }

    private fun setInitialValueToAdapter() {
        searchViewModel.keywords.value.let {
            (keywordRecyclerView.adapter as? SearchKeywordAdapter)?.submitList(it)
        }
    }

    private fun initiateSaveKeywordRecyclerView() {
        val adapter =
            SearchKeywordAdapter(LayoutInflater.from(this), object : SearchKeywordClickCallback {
                override fun clickKeyword(keyword: String) {
                    searchViewModel.clickKeyword(keyword)
                }

                override fun clickRemove(keyword: String) {
                    searchViewModel.clickKeywordDeleteButton(keyword)
                }
            })

        keywordRecyclerView.adapter = adapter
        keywordRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setKeywordRecyclerViewActive(active: Boolean) {
        keywordRecyclerView.isVisible = active
    }

    private fun initiateSearchView() {
        searchInput.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchViewModel.submitQuery(query ?: "")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        searchInput.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                searchViewModel.switchContent(SearchActivityViewModel.ContentType.SEARCH_LIST)
            }
        }
    }

    private fun updateText(name: String, address: String) {
        locationInfoNameView.text = name
        locationInfoAddressView.text = address
    }

    private fun initiateFragments() {
        mapFragment = KakaoMapFragment()
        searchFragment = SearchResultFragment()
    }

    private fun initiateViews() {
        locationInfoNameView = findViewById(R.id.text_location_name)
        locationInfoAddressView = findViewById(R.id.text_location_address)
        searchInput = findViewById(R.id.input_search)
        keywordRecyclerView = findViewById(R.id.saved_search_bar)
        searchResultFragmentContainer = findViewById(R.id.fragment_container_search_result)
        keywordRecyclerView = findViewById(R.id.saved_search_bar)
        initiateSearchView()
        initiateSaveKeywordRecyclerView()
    }

    private fun switchToSearchMenu() {
        val fragmentReplaceTransaction = fragmentManager.beginTransaction()
        fragmentReplaceTransaction.replace(R.id.fragment_container_search_result, searchFragment)
        fragmentReplaceTransaction.commit()
    }

    private fun switchToMapMenu() {
        val fragmentReplaceTransaction = fragmentManager.beginTransaction()
        fragmentReplaceTransaction.replace(R.id.fragment_container_search_result, mapFragment)
        fragmentReplaceTransaction.commit()
    }

    private fun checkKakaoSdk() {
        try {
            KakaoMapSdk.init(this, resources.getString(R.string.KAKAO_API_KEY))
        } catch (e: Exception) {
            Log.e("KSC", e.message ?: "")
            Log.e("KSC", e.stackTraceToString())
        }
    }
}
