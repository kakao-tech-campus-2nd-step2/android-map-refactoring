package campus.tech.kakao.map.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.PlaceLayoutBinding
import campus.tech.kakao.map.domain.Place
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlaceActivity : AppCompatActivity() {
    private lateinit var placeAdapter: PlaceRecyclerViewAdapter
    private lateinit var searchAdapter: SearchRecyclerViewAdapter
    private lateinit var placeBinding: PlaceLayoutBinding
    private val viewModel: PlaceViewModel by viewModels()

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        placeBinding = DataBindingUtil.setContentView(this, R.layout.place_layout)

        val searchList: MutableList<Place> = mutableListOf()
        val placeList: MutableList<Place> = mutableListOf()


        // Search 어댑터
        searchAdapter = SearchRecyclerViewAdapter(
            places = searchList,
            onItemClick = { place: Place ->
                placeBinding.etSearch.setText(place.name)
                placeBinding.etSearch.setSelection(place.name.length)
            },
            onCloseButtonClick = { place: Place ->
                viewModel.removePlaceRecord(place)
            }
        )
        placeBinding.rvSearchList.adapter = searchAdapter

        // Place 어댑터
        placeAdapter = PlaceRecyclerViewAdapter(
            places = placeList,
            onItemClick = { place ->
                viewModel.addPlaceRecord(place)
                val intent = Intent(this, MapActivity::class.java).apply {
                    putExtra("name", place.name)
                    putExtra("address", place.address)
                    putExtra("category", place.category)
                    putExtra("latitude", place.x)
                    putExtra("longitude", place.y)
                }
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        )
        placeBinding.rvPlaceList.adapter = placeAdapter

        viewModel.uiModel.observe(this, Observer { uiModel ->
            uiModel.searchList.let { searchList ->
                searchAdapter.updateData(searchList)
            }
            uiModel.placeList.let { placeList ->
                placeAdapter.updateData(placeList)
            }
            if (uiModel.isPlaceListVisible) {
                placeBinding.rvPlaceList.visibility = View.VISIBLE
                placeBinding.tvNoData.visibility = View.GONE
            }
            else {
                placeBinding.rvPlaceList.visibility = View.INVISIBLE
                placeBinding.tvNoData.visibility = View.VISIBLE
            }
            if (uiModel.isSearchListVisible) {
                placeBinding.rvSearchList.visibility = View.VISIBLE
            }
            else {
                placeBinding.rvSearchList.visibility = View.GONE
            }
        })

        placeBinding.btnErase.setOnClickListener {
            placeBinding.etSearch.setText("")
            viewModel.searchPlace(" ")
        }

        placeBinding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.searchPlace(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }
}