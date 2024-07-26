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
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
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
    lateinit var savedPlaceRecyclerView: RecyclerView
    lateinit var searchRecyclerView: RecyclerView
    lateinit var searchDeleteButton: ImageView
    lateinit var savedPlaceRecyclerViewAdapter: SavedPlaceViewAdapter
    lateinit var searchRecyclerViewAdapter: PlaceViewAdapter
    private val viewModel: SearchActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search)

        initVar()
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

    fun initVar() {
        searchRecyclerView = findViewById<RecyclerView>(R.id.search_result_recyclerView)
        savedPlaceRecyclerView = findViewById<RecyclerView>(R.id.saved_search_recyclerView)
        searchDeleteButton = findViewById<ImageView>(R.id.button_X)
    }

    fun initListeners() {
        initDeleteButtonListener()
        initInputFieldListener()
    }

    fun initDeleteButtonListener() {
        searchDeleteButton.setOnClickListener {
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
        searchRecyclerView.layoutManager = LinearLayoutManager(this)
        searchRecyclerView.adapter = searchRecyclerViewAdapter
    }

    fun initSavedPlaceRecyclerView() {
        savedPlaceRecyclerViewAdapter =
            SavedPlaceViewAdapter(this)
        savedPlaceRecyclerView.layoutManager =
            LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        savedPlaceRecyclerView.adapter = savedPlaceRecyclerViewAdapter
    }

    fun initObserver(){
        initPlaceObserver()
        initSavedPlaceObserver()
    }

    fun initPlaceObserver(){
        viewModel.place.observe(this, Observer {
            Log.d("readData", "검색창 결과 변경 감지")
            val placeList = viewModel.place.value
            Log.d("testt", "${placeList}")
            searchRecyclerViewAdapter.submitList(placeList)
            if (placeList?.isEmpty() == true) binding.noSearchResult.visibility = View.VISIBLE
            else binding.noSearchResult.visibility = View.INVISIBLE
        })
    }

    fun initSavedPlaceObserver(){
        viewModel.savedPlace.observe(this, Observer {
            Log.d("readData", "저장된 장소들 변경 감지")
            val savedPlace = viewModel.savedPlace.value
            savedPlaceRecyclerViewAdapter.submitList(savedPlace)
            if (savedPlace?.isEmpty() == true) savedPlaceRecyclerView.visibility = View.GONE
            else savedPlaceRecyclerView.visibility = View.VISIBLE
        })
    }
}





