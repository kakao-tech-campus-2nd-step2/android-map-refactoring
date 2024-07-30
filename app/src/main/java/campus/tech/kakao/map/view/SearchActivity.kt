package campus.tech.kakao.map.view

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.R
import campus.tech.kakao.map.adapter.PlaceViewAdapter
import campus.tech.kakao.map.adapter.SavedPlaceViewAdapter
import campus.tech.kakao.map.db.PlaceDBHelper
import campus.tech.kakao.map.model.Constants
import campus.tech.kakao.map.model.Place
import campus.tech.kakao.map.model.SavedPlace
import campus.tech.kakao.map.repository.PlaceRepository
import campus.tech.kakao.map.repository.SavedPlaceRepository
import campus.tech.kakao.map.viewmodel.SearchActivityViewModel
import campus.tech.kakao.map.viewmodel.SearchViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SearchActivity : AppCompatActivity(), OnClickPlaceListener, OnClickSavedPlaceListener {
    lateinit var noResultText: TextView
    lateinit var inputSearchField: EditText
    lateinit var savedPlaceRecyclerView: RecyclerView
    lateinit var searchRecyclerView: RecyclerView
    lateinit var searchDeleteButton: ImageView
    lateinit var savedPlaceRecyclerViewAdapter: SavedPlaceViewAdapter
    lateinit var searchRecyclerViewAdapter: PlaceViewAdapter
    private val viewModel: SearchActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        initVar()
        initListeners()
        initRecyclerViews()
        initObserver()
        inputSearchField.requestFocus()
    }

    override fun deleteSavedPlace(savedPlace: SavedPlace, position: Int) {
        Log.d("testt", "삭제 콜백함수 처리")
        viewModel.deleteSavedPlace(savedPlace)
    }

    override fun loadPlace(savedPlace: SavedPlace){
        inputSearchField.setText(savedPlace.name)
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
        noResultText = findViewById<TextView>(R.id.no_search_result)
        searchRecyclerView = findViewById<RecyclerView>(R.id.search_result_recyclerView)
        inputSearchField = findViewById<EditText>(R.id.input_search_field)
        savedPlaceRecyclerView = findViewById<RecyclerView>(R.id.saved_search_recyclerView)
        searchDeleteButton = findViewById<ImageView>(R.id.button_X)
    }

    fun initListeners() {
        initDeleteButtonListener()
        initInputFieldListener()
    }

    fun initDeleteButtonListener() {
        searchDeleteButton.setOnClickListener {
            inputSearchField.setText("")
            inputSearchField.clearFocus()
            inputSearchField.parent.clearChildFocus(inputSearchField)
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(window.decorView.applicationWindowToken, 0)
        }
    }

    fun initInputFieldListener() {
        inputSearchField.addTextChangedListener(object : TextWatcher {
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
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.place.collect { placeList ->
                    searchRecyclerViewAdapter.submitList(placeList)
                    if (placeList.isEmpty()) noResultText.visibility = View.VISIBLE
                    else noResultText.visibility = View.INVISIBLE
                }
            }
        }
    }

    fun initSavedPlaceObserver(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.savedPlace.collect { savedPlaceList ->
                    savedPlaceRecyclerViewAdapter.submitList(savedPlaceList)
                    if (savedPlaceList.isEmpty()) savedPlaceRecyclerView.visibility = View.GONE
                    else savedPlaceRecyclerView.visibility = View.VISIBLE
                }
            }
        }
    }
}





