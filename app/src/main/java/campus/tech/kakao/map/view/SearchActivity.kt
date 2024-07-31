package campus.tech.kakao.map.view

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.ActivitySearchBinding
import campus.tech.kakao.map.utilities.Constants
import campus.tech.kakao.map.data.Place
import campus.tech.kakao.map.data.SavedPlace
import campus.tech.kakao.map.viewmodel.SearchActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchActivity : AppCompatActivity(), OnClickPlaceListener, OnClickSavedPlaceListener {
    private lateinit var binding : ActivitySearchBinding
    lateinit var savedPlaceRecyclerViewAdapter: SavedPlaceViewAdapter
    lateinit var searchRecyclerViewAdapter: PlaceViewAdapter
    private val viewModel: SearchActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
        initListeners()
        initRecyclerViews()
        initObserver()
        binding.inputSearchField.requestFocus()
    }

    override fun deleteSavedPlace(savedPlace: SavedPlace) {
        Log.d("testt", "삭제 콜백함수 처리")
        viewModel.deleteSavedPlace(savedPlace)
    }

    override fun loadPlace(savedPlace: SavedPlace){
        binding.inputSearchField.setText(savedPlace.name)
    }

    override fun savePlace(place: Place) {
        Log.d("testt", "콜백함수 처리")
        viewModel.savePlace(place)
        intent.putExtra(Constants.Keys.KEY_PLACE, place)
        Log.d("testt", "Intent" + place.toString())
        setResult(RESULT_OK, intent)
        finish()
    }

    fun initBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    fun initListeners() {
        initDeleteButtonListener()
        initInputFieldListener()
    }

    fun initDeleteButtonListener() {
        binding.buttonX.setOnClickListener {
            binding.inputSearchField.setText("")
            binding.inputSearchField.clearFocus()
            binding.inputSearchField.parent.clearChildFocus(binding.inputSearchField)
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(window.decorView.applicationWindowToken, 0)
        }
    }

    fun initInputFieldListener() {
        binding.inputSearchField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(searchText: Editable?) {
                val text = searchText.toString()
                Log.d("inputField", "text : ${text}")
                Log.d("coroutine", "입력변경")
                lifecycleScope.launch {
                    viewModel.getKakaoLocalData(text)
                }
            }
        })
    }

    fun initRecyclerViews() {
        initSearchRecyclerView()
        initSavedPlaceRecyclerView()
    }

    fun initSearchRecyclerView() {
        searchRecyclerViewAdapter = PlaceViewAdapter(this)
        binding.searchResultRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.searchResultRecyclerView.adapter = searchRecyclerViewAdapter
    }

    fun initSavedPlaceRecyclerView() {
        savedPlaceRecyclerViewAdapter =
            SavedPlaceViewAdapter(this)
        binding.savedSearchRecyclerView.layoutManager =
            LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding.savedSearchRecyclerView.adapter = savedPlaceRecyclerViewAdapter
    }

    fun initObserver(){
        initPlaceObserver()
        initSavedPlaceObserver()
    }

    fun initPlaceObserver(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.place.collect { placeList ->
                    searchRecyclerViewAdapter.submitList(placeList)
                }
            }
        }
    }

    fun initSavedPlaceObserver(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.savedPlace.collect { savedPlaceList ->
                    savedPlaceRecyclerViewAdapter.submitList(savedPlaceList)
                    if (savedPlaceList.isEmpty()) binding.savedSearchRecyclerView.visibility = View.GONE
                    else binding.savedSearchRecyclerView.visibility = View.VISIBLE
                }
            }
        }
    }
}





