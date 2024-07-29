package campus.tech.kakao.map.view

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.ActivitySearchBinding
import campus.tech.kakao.map.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {
    private val viewModel: SearchViewModel by viewModels()
    private lateinit var binding: ActivitySearchBinding
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var savePlaceAdapter: SavePlaceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        initView()
        setListeners()
        setAdapters()
        observeViewModel()
    }

    private fun initView() {
        binding.searchPlaceView.layoutManager = LinearLayoutManager(this)
        binding.savePlaceView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setListeners() {
        binding.cancelBtn.setOnClickListener {
            binding.searchText.setText("")
            updateViewVisibility(false)
        }

        binding.searchText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    updateViewVisibility(false)
                } else {
                    viewModel.getPlaceList(s.toString())
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setAdapters() {
        searchAdapter = SearchAdapter(emptyList()) {
            viewModel.savePlaces(it.place_name)
            val intent = Intent(this, MapActivity::class.java)
            intent.putExtra("placeInfo", it)
            startActivity(intent)
        }
        binding.searchPlaceView.adapter = searchAdapter

        savePlaceAdapter = SavePlaceAdapter(emptyList()) {
            viewModel.deleteSavedPlace(it.savePlaceName)
        }
        binding.savePlaceView.adapter = savePlaceAdapter
    }

    private fun observeViewModel() {
        viewModel.places.observe(this) { places ->
            searchAdapter.updateData(places)
            updateViewVisibility(places.isNotEmpty())
        }

        viewModel.savePlaces.observe(this) { savePlaces ->
            savePlaceAdapter.updateData(savePlaces)
        }
    }

    private fun updateViewVisibility(hasPlaces: Boolean) {
        if (hasPlaces) {
            binding.searchPlaceView.visibility = View.VISIBLE
            binding.noSearch.visibility = View.GONE
        } else {
            binding.searchPlaceView.visibility = View.GONE
            binding.noSearch.visibility = View.VISIBLE
        }
    }
}
