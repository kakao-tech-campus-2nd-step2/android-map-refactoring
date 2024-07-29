package campus.tech.kakao.map.view

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import campus.tech.kakao.map.adapter.listener.RecentAdapterListener
import campus.tech.kakao.map.adapter.listener.SearchAdapterListener
import campus.tech.kakao.map.adapter.RecentSearchAdapter
import campus.tech.kakao.map.adapter.SearchDataAdapter
import campus.tech.kakao.map.data.LocationDataContract
import campus.tech.kakao.map.databinding.ActivityDataSearchBinding
import campus.tech.kakao.map.repository.SearchResultRepository
import campus.tech.kakao.map.viewModel.DBViewModel
import campus.tech.kakao.map.viewModel.SearchViewModel
import campus.tech.kakao.map.viewModel.factory.SearchViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import androidx.activity.viewModels

@AndroidEntryPoint
class DataSearchActivity : AppCompatActivity(), RecentAdapterListener, SearchAdapterListener {
    private lateinit var binding: ActivityDataSearchBinding

    private lateinit var searchViewModel: SearchViewModel   //검색 결과 관리  //추후 계획: step2에서 수정이 있었던 부분이기 때문에, step2에서 Hilt 적용
    private val recentViewModel: DBViewModel by viewModels()  //최근 검색어 관리

    private lateinit var searchResultRepo: SearchResultRepository   //검색 결과 관리
    private lateinit var searchResultDataAdapter: SearchDataAdapter   //검색 결과 표시 위함

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setBind()

        //ViewModel 생성
        searchResultRepo = SearchResultRepository()
        searchViewModel = ViewModelProvider(
            this,
            SearchViewModelFactory(searchResultRepo)
        )[SearchViewModel::class.java]

        //RecyclerView Layout 매니저 설정 (스크롤 방향 설정)
        binding.searchResulListView.layoutManager = LinearLayoutManager(this)
        binding.recentSearchListView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        //Adapter 초기화
        searchResultDataAdapter = SearchDataAdapter(emptyList(), recentViewModel, this)
        binding.searchResulListView.adapter = searchResultDataAdapter
        binding.recentSearchListView.adapter =
            RecentSearchAdapter(emptyList(), recentViewModel, this)

        resetButtonListener()   //x버튼 눌러 검색 입력 필드 내용 삭제
        setTextWatcher()    //검색 입력 필드 텍스트 변경 리스너

        //검색 결과 데이터 관찰 -> searchResultAdapter 데이터 넘기기
        searchViewModel.getSearchDataLiveData().observe(this, Observer { documentsList ->
            if (documentsList.isNotEmpty()) {
                binding.noResult.visibility = View.GONE
                searchResultDataAdapter.updateData(documentsList)
            } else {
                binding.noResult.visibility = View.VISIBLE
                searchResultDataAdapter.updateData(emptyList())
            }
        })

        //검색 history 데이터 관찰
        recentViewModel.getRecentDataLiveData().observe(this, Observer { recentData ->
            binding.recentSearchListView.adapter =
                RecentSearchAdapter(recentData, recentViewModel, this)
        })
    }

    private fun setBind(){
        binding = ActivityDataSearchBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    private fun setTextWatcher() {
        binding.searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchInput = binding.searchBar.text.trim().toString()

                if (searchInput.isNotEmpty()) {
                    searchViewModel.loadResultData(searchInput)
                } else {
                    binding.noResult.visibility = View.VISIBLE
                    searchResultDataAdapter.updateData(emptyList())
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    private fun resetButtonListener() {
        binding.deleteInput.setOnClickListener {
            binding.searchBar.text.clear()
        }
    }

    //클릭한 검색어가 자동으로 입력되는 기능 구현
    override fun autoSearch(searchData: String) {
        binding.searchBar.setText(searchData)
    }

    override fun displaySearchLocation(
        name: String,
        address: String,
        latitude: String,
        longitude: String
    ) {
        val intent = Intent(this@DataSearchActivity, HomeMapActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra(LocationDataContract.LOCATION_NAME, name)
        intent.putExtra(LocationDataContract.LOCATION_ADDRESS, address)
        intent.putExtra(LocationDataContract.LOCATION_LATITUDE, latitude)
        intent.putExtra(LocationDataContract.LOCATION_LONGITUDE, longitude)
        startActivity(intent)
    }
}
