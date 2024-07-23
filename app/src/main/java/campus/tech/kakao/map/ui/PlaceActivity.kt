package campus.tech.kakao.map.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import campus.tech.kakao.map.R
import campus.tech.kakao.map.data.NetworkRepository
import campus.tech.kakao.map.data.Place
import campus.tech.kakao.map.data.PlaceDBClient
import campus.tech.kakao.map.data.PlaceDao
import campus.tech.kakao.map.databinding.SearchLayoutBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class PlaceActivity : AppCompatActivity() {
    private lateinit var placeAdapter: PlaceRecyclerViewAdapter
    private lateinit var searchAdapter: SearchRecyclerViewAdapter
    private lateinit var placeBinding: SearchLayoutBinding
    private lateinit var placeDao: PlaceDao

    @Inject
    lateinit var networkRepository: NetworkRepository

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        placeBinding = DataBindingUtil.setContentView(this, R.layout.search_layout)
        placeDao = PlaceDBClient.getInstance(applicationContext).placeDao()


        val searchList: MutableList<Place> = mutableListOf()
        val keywordList: MutableList<Place> = mutableListOf()

        // Search 어댑터
        searchAdapter = searchRecyclerViewAdapter(searchList)
        searchAdapter = searchRecyclerViewAdapter(searchList)
        placeBinding.rvSearchList.adapter = searchAdapter
        placeBinding.rvSearchList.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        // Place 어댑터
        placeAdapter = placeRecyclerViewAdapter(keywordList, searchList)
        placeBinding.rvPlaceList.adapter = placeAdapter
        placeBinding.rvPlaceList.layoutManager = LinearLayoutManager(this)

        // DAO로 데이터 접근
        lifecycleScope.launch {
            val places = placeDao.getAllPlaces()
            withContext(Dispatchers.Main) {
                searchList.clear()
                searchList.addAll(places)
                searchAdapter.notifyDataSetChanged()
                controlPlaceVisibility(keywordList)
                controlSearchVisibility(searchList)
            }
        }

        placeBinding.btnErase.setOnClickListener {
            val emptyKeyword = " "
            placeBinding.etSearch.setText(emptyKeyword)
            searchPlace(emptyKeyword)
        }

        placeBinding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val keyword = s.toString()
                Log.d("API response", "$keyword")
                searchPlace(keyword)
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    private fun searchPlace(keyword: String) {
        networkRepository.searchPlace(keyword,
            onSuccess = { keywordList ->
                placeAdapter.updateData(keywordList)
                placeAdapter.notifyDataSetChanged()
                controlPlaceVisibility(keywordList)
            },
            onFailure = { throwable ->
                Log.w("API response", "Failure: $throwable")
            }
        )
    }

    private fun placeRecyclerViewAdapter(placeList: MutableList<Place>, searchList: MutableList<Place>) =
        PlaceRecyclerViewAdapter(
            placeList,
            onItemClick = { place ->
                // 장소 목록 선택 시, 검색어 기록 저장
                if (place in searchList) {
                    removePlaceRecord(searchList, place)
                }
                addPlaceRecord(searchList, place)
                controlSearchVisibility(searchList)

                // 장소 목록 선택 시, 해당 항목의 위치를 지도에 표시 -> !!!!!!
                Log.d("place", "${place.x}, ${place.y}")
                val mapIntent = Intent(this, MapActivity::class.java).apply {
                    putExtra("name", place.name)
                    putExtra("address", place.address)
                    putExtra("category", place.category)
                    putExtra("latitude", place.x)
                    putExtra("longitude", place.y)
                }
                startActivity(mapIntent)
            }
        )

    private fun searchRecyclerViewAdapter(searchList: MutableList<Place>) =
        SearchRecyclerViewAdapter(
            searchList,
            // 저장 목록 선택 시, 검색칸에 장소명 표시
            onItemClick = { place ->
                placeBinding.etSearch.setText(place.name)
                placeBinding.etSearch.setSelection(place.name.length)
            },
            // X 선택 시, 저장 목록에서 삭제
            onCloseButtonClick = { place ->
                removePlaceRecord(searchList, place)
                controlSearchVisibility(searchList)
            }
        )

    // 검색 저장 기록 조작
    fun addPlaceRecord(searchList: MutableList<Place>, place: Place) {
        searchList.add(place)
        lifecycleScope.launch {
            placeDao.insertPlace(place)
        }
        searchAdapter.notifyDataSetChanged()
    }

    fun removePlaceRecord(searchList: MutableList<Place>, place: Place) {
        val index = searchList.indexOf(place)
        searchList.removeAt(index)
        lifecycleScope.launch {
            placeDao.deletePlace(place)
        }
        searchAdapter.notifyDataSetChanged()
    }

    // visibility 조작
    fun controlPlaceVisibility(placeList: List<Place>) {
        if (placeList.isEmpty()) {
            placeBinding.rvPlaceList.visibility = View.INVISIBLE
            placeBinding.tvNoData.visibility = View.VISIBLE
        }
        else {
            placeBinding.rvPlaceList.visibility = View.VISIBLE
            placeBinding.tvNoData.visibility = View.GONE
        }
    }

    fun controlSearchVisibility(searchList: List<Place>) {
        if (searchList.isEmpty()) {
            placeBinding.rvSearchList.visibility = View.GONE
        }
        else {
            placeBinding.rvSearchList.visibility = View.VISIBLE
        }
    }
}