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
        viewModel = ViewModelProvider(this).get(MyViewModel::class.java)    //이게 뭘까
        binding.viewModel = viewModel

//        setContentView(R.layout.activity_search_place)
//        val search = findViewById<EditText>(R.id.search)
//        val closeIcon = findViewById<ImageView>(R.id.close_icon)
//        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
//        val savedSearch = findViewById<RecyclerView>(R.id.saved_search)

        val dbManager = DatabaseManager(context = this)

        viewModel.isIntent.value = false //인텐트를 위한 조정

        //Place 리사이클러뷰 설정
        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        placeAdapter = PlaceAdapter(emptyList()) { place ->  //리사이클러뷰의 아이템을 누르면
            dbManager.insertSavedPlace(place.id, place.name)
            viewModel.updateSavedSearch(dbManager)
        }
        binding.recyclerView.adapter = placeAdapter

//        placeAdapter = PlaceAdapter(emptyList()){ place ->
//            dbManager.insertSavedPlace(place.id, place.name)
//            updateSavedSearch(dbManager)
//        }
//        recyclerView.layoutManager = LinearLayoutManager(this)
//        recyclerView.adapter = placeAdapter

        //저장된 검색어 설정
        val savedSearch = binding.savedSearch
        savedSearch.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        savedSearchAdapter = SavedSearchAdapter(emptyList()) { savedSearch -> //x를 누르면
            dbManager.deleteSavedPlace(savedSearch.id)
            viewModel.updateSavedSearch(dbManager)
        }
        binding.savedSearch.adapter = savedSearchAdapter

        //Place 리사이클러뷰 업데이트 관찰
        viewModel.placeAdapterUpdateData.observe(this, Observer {
            placeAdapter.updateData(it)
        })

        //저장된 검색어 관찰
        viewModel.savedSearchAdapterUpdateData.observe(this, Observer {
            savedSearchAdapter.updateData(it)
        })

        //editText에서 변경 감지
        viewModel.searchText.observe(this, Observer {
            Log.d("setyoun", "editText : $it")
            if (it == "") {
                binding.search.text.clear()
            } else viewModel.searchPlaces(it)
        })

        //저장된 검색어 adapter, recyclerView
//        savedSearchAdapter = SavedSearchAdapter(emptyList()){ savedSearch ->
//            dbManager.deleteSavedPlace(savedSearch.id)
//            updateSavedSearch(dbManager)
//        }
//        savedSearch.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)    //마지막 인자는 역순으로 배치 여부
//        savedSearch.adapter = savedSearchAdapter

        //검색어 지우기
//        closeIcon.setOnClickListener {
//            it.performHapticFeedback(android.view.HapticFeedbackConstants.VIRTUAL_KEY)  //가벼운 진동
//            search.text.clear()
//            //불필요한 서버요청이 일어나고 있다.. 수정하기
//            searchPlaces(" ")    //close 누르고 recyclerView도 비우기
//        } 구현해야함!!!!!!!!

        //검색창에 텍스트가 바뀔 때마다 감지해서 검색
//        search.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(s: Editable?) {
//                // 텍스트 변경 후 호출
//                val query = s.toString()
//                searchPlaces(query)
//            }
//
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
//        })
//        updateSavedSearch(dbManager)


    } //onCreate

//    private fun searchPlaces(query: String) {
//        val call = RetrofitInstance.api.searchKeyword(query) //API 요청
//
//        call.enqueue(object : Callback<KakaoSearchResponse> { //비동기 API 요청
//
//            override fun onResponse(call: Call<KakaoSearchResponse>, response: Response<KakaoSearchResponse>) {
//
//                Log.d("seyoung","query : $query")
//                if (response.isSuccessful) {    //성공했을 때
//                    val places = response.body()?.documents?.map { document ->
//                        Place(
//                            id = document.id.toInt(),
//                            name = document.place_name,
//                            address = document.address_name,
//                            kind = document.category_name
//                        )
//                    } ?: emptyList()
//                    placeAdapter.updateData(places)
//                }
//                else {  //실패했을 때
//                    Log.d("seyoung", "Error: ${response.errorBody()?.string()}")
//                }
//
//            }
//
//            override fun onFailure(call: Call<KakaoSearchResponse>, t: Throwable) { //실패 했을 때 (네트워크 문제, 비행기 모드...)
//                Log.d("seyoung", "Failure: ${t.message}")
//                Toast.makeText(this@SearchPlaceActivity,"네트워크를 확인해주세요",Toast.LENGTH_SHORT).show()
//            }
//        })
//    }
//
//
//    //저장된 검색어 업데이트
//    private fun updateSavedSearch(dbManager: DatabaseManager) {
//        val savedSearches = dbManager.getSavedSearches()
//        savedSearchAdapter.updateData(savedSearches)
//    }
}
