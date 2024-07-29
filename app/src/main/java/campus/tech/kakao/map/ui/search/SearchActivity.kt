package campus.tech.kakao.map.ui.search

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import campus.tech.kakao.map.databinding.ActivitySearchBinding
import campus.tech.kakao.map.domain.model.LocationDomain
import campus.tech.kakao.map.domain.model.PlaceDomain
import campus.tech.kakao.map.domain.model.SavedSearchWordDomain
import campus.tech.kakao.map.ui.IntentKeys.EXTRA_LOCATION
import campus.tech.kakao.map.ui.search.adapters.ResultRecyclerViewAdapter
import campus.tech.kakao.map.ui.search.adapters.SavedSearchWordRecyclerViewAdapter
import campus.tech.kakao.map.ui.search.interfaces.OnPlaceItemClickListener
import campus.tech.kakao.map.ui.search.interfaces.OnSavedSearchWordClearImageViewClickListener
import campus.tech.kakao.map.ui.search.interfaces.OnSavedSearchWordTextViewClickListener
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding

    private val placeViewModel: PlaceViewModel by viewModels()
    private val savedSearchWordViewModel: SavedSearchWordViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViews()
        setupObservers()
    }

    /**
     * RecyclerView들을 설정하는 함수.
     */
    private fun setupRecyclerViews() {
        setSearchResultRecyclerView()
        setSavedSearchWordRecyclerView()
    }

    /**
     * view들을 설정하는 함수.
     */
    private fun setupViews() {
        setClearImageViewClickListener()
        setSearchEditText()
        setupRecyclerViews()
    }

    /**
     * 검색 EditText가 변경되면 placeViewModel을 통해 검색을 수행하도록 하는 함수.
     */
    private fun setSearchEditText() {
        binding.searchEditText.addTextChangedListener { editable ->
            val categoryInput = editable.toString().trim()
            placeViewModel.updateCategoryInput(categoryInput, totalPageCount = 2)
        }
    }

    /**
     * clearImageView의 클릭 리스너를 설정하는 함수.
     *
     * searchEditText의 text를 null로 변경.
     */
    private fun setClearImageViewClickListener() {
        binding.searchClearImageView.setOnClickListener {
            binding.searchEditText.text = null
        }
    }

    /**
     * 검색 결과를 표시하는 RecyclerView를 설정하는 함수.
     *
     * - `placeItemClickListener` : placeItem을 누르면 검색어가 저장되고 해당 위치를 전달하여 MapActivity로 이동하는 클릭 리스너 interface 구현 객체
     */
    private fun setSearchResultRecyclerView() {
        val placeItemClickListener =
            object : OnPlaceItemClickListener {
                override fun onPlaceItemClicked(place: PlaceDomain) {
                    savedSearchWordViewModel.handleUiEvent(
                        SavedSearchWordViewModel.UiEvent.OnPlaceItemClicked(place.toSavedSearchWordDomain()),
                    )
                }
            }
        binding.searchResultRecyclerView.adapter = ResultRecyclerViewAdapter(placeItemClickListener)
        binding.searchResultRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    /**
     * MapActivity로 이동하는 함수.
     *
     * @param savedSearchWord 이동할 장소의 정보를 담고 있는 SavedSearchWord 객체.
     */
    private fun navigateToMapActivity(savedSearchWord: SavedSearchWordDomain) {
        val intent = Intent()
        intent.putExtra(EXTRA_LOCATION, savedSearchWord.toLocationDomain())
        setResult(RESULT_OK, intent)
        finish()
    }

    /**
     * SavedSearchWordRecyclerView를 설정하는 함수.
     *
     * - `savedSearchWordClearImageViewClickListener` : clear 버튼을 누르면 해당 저장된 검색어가 사라지도록 하는 클릭리스너 interface 구현 객체.
     * - `savedSearchWordTextViewClickListener` : textView를 누르면 해당 위치를 전달하여 MapActivity로 이동하는 클릭 리스너 interface 구현 객체.
     */
    private fun setSavedSearchWordRecyclerView() {
        val savedSearchWordClearImageViewClickListener =
            object : OnSavedSearchWordClearImageViewClickListener {
                override fun onSavedSearchWordClearImageViewClicked(savedSearchWord: SavedSearchWordDomain) {
                    savedSearchWordViewModel.handleUiEvent(
                        SavedSearchWordViewModel.UiEvent.OnSavedSearchWordClearImageViewClicked(savedSearchWord),
                    )
                }
            }
        val savedSearchWordTextViewClickListener =
            object : OnSavedSearchWordTextViewClickListener {
                override fun onSavedSearchWordTextViewClicked(savedSearchWord: SavedSearchWordDomain) {
                    navigateToMapActivity(savedSearchWord)
                }
            }
        binding.savedSearchWordRecyclerView.adapter =
            SavedSearchWordRecyclerViewAdapter(
                savedSearchWordClearImageViewClickListener,
                savedSearchWordTextViewClickListener,
            )
        binding.savedSearchWordRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    /**
     * viewModel을 관찰하도록 하는 함수.
     */
    private fun setupObservers() {
        collectSearchResults()
        collectSavedSearchWords()
        collectSideEffects()
        collectErrorMessages()
    }

    /**
     * 검색 결과를 관찰하고, RecyclerView에 결과를 반영하는 함수.
     */
    private fun collectSearchResults() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                placeViewModel.searchResults.collectLatest { places ->
                    (binding.searchResultRecyclerView.adapter as? ResultRecyclerViewAdapter)?.submitList(
                        places,
                    )
                    binding.noSearchResultTextView.visibility =
                        if (places.isEmpty()) View.VISIBLE else View.GONE
                }
            }
        }
    }

    /**
     * 저장된 검색어를 관찰하고, RecyclerView에 결과를 반영하는 함수.
     */
    private fun collectSavedSearchWords() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                savedSearchWordViewModel.savedSearchWords.collectLatest { savedSearchWords ->
                    (binding.savedSearchWordRecyclerView.adapter as? SavedSearchWordRecyclerViewAdapter)?.submitList(
                        savedSearchWords,
                    )
                    binding.savedSearchWordRecyclerView.visibility =
                        if (savedSearchWords.isEmpty()) View.GONE else View.VISIBLE
                }
            }
        }
    }

    /**
     * viewModel의 사이드 이펙트를 관찰하고 처리하는 함수.
     */
    private fun collectSideEffects() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                savedSearchWordViewModel.sideEffects.collect { sideEffect ->
                    when (sideEffect) {
                        is SavedSearchWordViewModel.SideEffect.NavigateToMapActivity -> {
                            navigateToMapActivity(sideEffect.savedSearchWord)
                        }
                    }
                }
            }
        }
    }

    /**
     * viewModel의 에러를 관찰하고 처리하는 함수.
     */
    private fun collectErrorMessages() {
        collectPlaceErrorMessages()
        collectSavedSearchWrodErrorMessages()
    }

    private fun collectPlaceErrorMessages() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                savedSearchWordViewModel.errorMessage.collectLatest { errorMessage ->
                    errorMessage?.let {
                        Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun collectSavedSearchWrodErrorMessages() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                placeViewModel.errorMessage.collectLatest { errorMessage ->
                    errorMessage?.let {
                        Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    /**
     * Place 객체를 SavedSearchWord 객체로 변환하는 확장 함수.
     *
     * @return 변환된 SavedSearchWord 객체.
     */
    private fun PlaceDomain.toSavedSearchWordDomain(): SavedSearchWordDomain {
        return SavedSearchWordDomain(
            name = this.name,
            placeId = this.id,
            address = this.address,
            latitude = this.latitude,
            longitude = this.longitude,
        )
    }

    private fun SavedSearchWordDomain.toLocationDomain(): LocationDomain {
        return LocationDomain(
            name = this.name,
            latitude = this.latitude,
            longitude = this.longitude,
            address = this.address,
        )
    }
}
