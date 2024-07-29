package campus.tech.kakao.map.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import campus.tech.kakao.map.listener.RecentAdapterListener
import campus.tech.kakao.map.listener.SearchAdapterListener
import campus.tech.kakao.map.adapter.RecentSearchAdapter
import campus.tech.kakao.map.adapter.SearchDataAdapter
import campus.tech.kakao.map.dataContract.LocationDataContract
import campus.tech.kakao.map.databinding.ActivityDataSearchBinding
import campus.tech.kakao.map.viewModel.DBViewModel
import campus.tech.kakao.map.viewModel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DataSearchActivity : AppCompatActivity(), RecentAdapterListener, SearchAdapterListener {
    private lateinit var binding: ActivityDataSearchBinding

    private lateinit var searchViewModel: SearchViewModel   //추후 계획: step2에서 수정이 있었던 부분이기 때문에, step2에서 Hilt 적용
    private val recentViewModel: DBViewModel by viewModels()

    private lateinit var resultDataAdapter: SearchDataAdapter


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setBind()

        //ViewModel 생성 //추후 계획: step2에서 수정이 있었던 부분이기 때문에, step2에서 Hilt 적용후 삭제
        searchViewModel = ViewModelProvider(this)[SearchViewModel::class.java]

        //검색 결과 목록 세로 스크롤 설정
        binding.searchResulListView.layoutManager = LinearLayoutManager(this)
        //최근 검색어 목록 가로 스크롤 설정
        binding.recentSearchListView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        //어뎁터 초기화
        resultDataAdapter = SearchDataAdapter(emptyList(), recentViewModel, this)
        binding.searchResulListView.adapter = resultDataAdapter

        resetButtonListener()
        setTextWatcher()

        recentViewModel.getRecentDataLiveData().observe(this, Observer { recentData ->
            binding.recentSearchListView.adapter = RecentSearchAdapter(recentData, recentViewModel, this)
        })

        searchViewModel.searchResults.observe(this, Observer { documentsList ->
            if (documentsList.isNotEmpty()) {
                binding.noResult.visibility = View.GONE
                resultDataAdapter.updateData(documentsList)
            } else {
                binding.noResult.visibility = View.VISIBLE
                resultDataAdapter.updateData(emptyList())
            }
        })

        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(this@DataSearchActivity, HomeMapActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }
        }
        this.onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
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
                    resultDataAdapter.updateData(emptyList())
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
