package campus.tech.kakao.map.ui.search

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.ActivitySearchLocationBinding
import campus.tech.kakao.map.ui.search.adapter.HistoryAdapter
import campus.tech.kakao.map.ui.search.adapter.SearchLocationAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchLocationActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModel: SearchLocationViewModel
    private lateinit var binding: ActivitySearchLocationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_location)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.removeSearchInputButton.setOnClickListener {
            binding.searchInputEditText.text.clear()
        }

        viewModel.location.observe(this) { locationData ->
            if (locationData == null) {
                val errorString = "데이터를 가져오지 못했습니다. 잠시 후 다시 시도해주세요"
                Toast.makeText(this, errorString, Toast.LENGTH_SHORT).show()
                return@observe
            }

            binding.searchResultRecyclerView.adapter =
                SearchLocationAdapter(locationData, this, viewModel)
        }

        viewModel.history.observe(this) {
            it?.let { historyData ->
                var adapter = binding.searchHistoryRecyclerView.adapter as? HistoryAdapter
                if (adapter == null) {
                    adapter = HistoryAdapter(this, viewModel)
                    binding.searchHistoryRecyclerView.adapter = adapter
                }

                adapter.submitList(historyData)
            }
        }

        viewModel.markerLocation.observe(this) {
            it?.let { location ->
                val intent = intent
                intent.putExtra("markerLocation", location)
                setResult(RESULT_OK, intent)
                finish()
            }
        }
    }
}