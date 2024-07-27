package campus.tech.kakao.map.ui

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.R
import campus.tech.kakao.map.viewmodel.MapViewModel
import campus.tech.kakao.map.databinding.ActivityMainBinding
import campus.tech.kakao.map.model.MapItem
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), SearchResultAdapter.OnItemClickListener,
    KeywordAdapter.OnKeywordRemoveListener {

    private val mapViewModel: MapViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    private val searchResultAdapter = SearchResultAdapter(this)
    private val keywordAdapter = KeywordAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.viewModel = mapViewModel

        binding.rvSearchResult.layoutManager = LinearLayoutManager(this)
        binding.rvSearchResult.adapter = searchResultAdapter

        binding.rvKeywords.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvKeywords.adapter = keywordAdapter

        mapViewModel.searchResults.observe(this, Observer { results ->
            if (results.isEmpty()) {
                binding.tvNoResults.visibility = TextView.VISIBLE
                binding.rvSearchResult.visibility = RecyclerView.GONE
            } else {
                binding.tvNoResults.visibility = TextView.GONE
                binding.rvSearchResult.visibility = RecyclerView.VISIBLE
                searchResultAdapter.submitList(results)
            }
        })

        mapViewModel.errorMessage.observe(this, Observer { message ->
            message?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        })

        mapViewModel.keywords.observe(this, Observer { keywords ->
            keywordAdapter.submitList(keywords)
        })

        binding.ivClear.setOnClickListener {
            mapViewModel.clearKeyword()
        }

        mapViewModel.loadKeywords()

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(this@MainActivity, MapActivity::class.java)
                startActivity(intent)
                finish()
            }
        })

        mapViewModel.getSavedMapItems()

        binding.etKeywords.setOnClickListener {
            mapViewModel.clearMapItems()
        }
    }

    override fun onItemClick(item: MapItem) {
        val newKeywords = keywordAdapter.currentKeywords.toMutableList()

        if (!newKeywords.contains(item.name)) {
            newKeywords.add(item.name)
            mapViewModel.saveKeywords(newKeywords)
        }

        val intent = Intent(this, MapActivity::class.java).apply {
            putExtra("name", item.name)
            putExtra("address", item.address)
            putExtra("longitude", item.longitude)
            putExtra("latitude", item.latitude)
        }
        startActivity(intent)
    }

    override fun onKeywordRemove(keyword: String) {
        val newKeywords = keywordAdapter.currentKeywords.toMutableList().apply { remove(keyword) }
        mapViewModel.saveKeywords(newKeywords)
    }

    override fun onKeywordClick(keyword: String) {
        binding.etKeywords.setText(keyword)
        mapViewModel.setKeyword(keyword)
    }
}