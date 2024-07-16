package campus.tech.kakao.map

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import campus.tech.kakao.map.databinding.ActivitySearchPlaceBinding

class SearchPlaceActivity : AppCompatActivity() {

    lateinit var binding: ActivitySearchPlaceBinding
    lateinit var viewModel: MyViewModel
    private lateinit var placeAdapter: PlaceAdapter
    private lateinit var savedSearchAdapter: SavedSearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_place)
        viewModel = ViewModelProvider(this).get(MyViewModel::class.java)
        binding.viewModel = viewModel
        val dbManager = DatabaseManager(context = this)
        viewModel.isIntent.value = false //인텐트를 위한 조정

        //Place 리사이클러뷰 설정
        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        placeAdapter = PlaceAdapter(emptyList(), viewModel)
        binding.recyclerView.adapter = placeAdapter

        viewModel.itemClick.observe(this, Observer {
            dbManager.insertSavedPlace(it.id, it.name)
            viewModel.updateSavedSearch(dbManager)
        })


        //savedSearch 저장된 검색어 설정
        val savedSearch = binding.savedSearch
        savedSearch.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        savedSearchAdapter = SavedSearchAdapter(emptyList(),viewModel)
        binding.savedSearch.adapter = savedSearchAdapter

        //savedSearch의 closeIcon 클릭 이벤트
        viewModel.closeClick.observe(this, Observer {
            dbManager.deleteSavedSearch(it.id)
            viewModel.updateSavedSearch(dbManager)
        })

        //savedSearch의 name 클릭 이벤트
        viewModel.nameClick.observe(this, Observer {
            binding.search.setText(it.name)
            viewModel.searchText.value = it.name
        })


        //Place 리사이클러뷰 업데이트 관찰
        viewModel.placeAdapterUpdateData.observe(this, Observer {
            placeAdapter.updateData(it)
        })

        //savedSearch 저장된 검색어 관찰
        viewModel.savedSearchAdapterUpdateData.observe(this, Observer {
            savedSearchAdapter.updateData(it)
        })

        //editText에서 변경 감지
        viewModel.searchText.observe(this, Observer {
            if (it == "") binding.search.text.clear()   //searchText가 비어있다면 화면에서도 지우기
            else viewModel.searchPlaces(it) //텍스트가 있다면 검색
        })


    } //onCreate

}
