package campus.tech.kakao.map.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.adapter.ItemClickListener
import campus.tech.kakao.map.Room.MapItemViewModel
import campus.tech.kakao.map.adapter.MapListAdapter
import campus.tech.kakao.map.R
import campus.tech.kakao.map.Room.KakaoMapItem
import campus.tech.kakao.map.Room.MapItem
import campus.tech.kakao.map.adapter.SelectItemClickListener
import campus.tech.kakao.map.adapter.SelectListAdapter
import campus.tech.kakao.map.SelectMapItem
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {
    private val mapItemViewModel: MapItemViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        //val mapItemViewModel = MapItemViewModel(this)

        val mapList = findViewById<RecyclerView>(R.id.mapList)
        val selectList = findViewById<RecyclerView>(R.id.selectList)
        val inputSpace = findViewById<EditText>(R.id.inputSpace)
        val mainText = findViewById<TextView>(R.id.main_text)
        val cancelBtn = findViewById<ImageView>(R.id.cancelBtnInputSpace)

        //어댑터 설정
        val mapListAdapter = MapListAdapter(listOf(), LayoutInflater.from(this))
        val selectListAdapter = SelectListAdapter(listOf(), LayoutInflater.from(this))

        mapList.adapter = mapListAdapter
        mapList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        selectList.adapter = selectListAdapter
        selectList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        //리스너 정의
        mapListAdapter.setItemClickListener(object : ItemClickListener {
            override fun onClick(v: View, mapItem: KakaoMapItem) {
                mapItemViewModel.insertSelectItem(
                    MapItem(
                        0,
                        mapItem.name,
                        mapItem.address,
                        mapItem.category,
                        mapItem.x,
                        mapItem.y,
                        mapItem.id
                    )
                )
                val intent = Intent(this@SearchActivity, MapActivity::class.java)
                intent.putExtra("x", mapItem.x.toDouble())
                intent.putExtra("y", mapItem.y.toDouble())
                intent.putExtra("name", mapItem.name)
                intent.putExtra("address", mapItem.address)
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                //finish()
            }
        })

        selectListAdapter.setCancelBtnClickListener(object : SelectItemClickListener {
            override fun onClick(v: View, selectItem: MapItem) {
                mapItemViewModel.deleteSelectItem(selectItem)
            }
        })

        selectListAdapter.setItemClickListener(object : SelectItemClickListener {
            override fun onClick(v: View, selectItem: MapItem) {
                inputSpace.setText(selectItem.name)
            }
        })

        // 옵저버 설정
        mapItemViewModel.kakaoMapItemList.observe(this) {
            mapListAdapter.updateMapItemList(it)
            if (mapItemViewModel.kakaoMapItemList.value.isNullOrEmpty()) {
                mainText.visibility = View.VISIBLE
            } else {
                mainText.visibility = View.INVISIBLE
            }
        }

        mapItemViewModel.selectItemList.observe(this) {
            selectListAdapter.updateMapItemList(it)
        }

        // EditText 리스너
        inputSpace.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                CoroutineScope(Dispatchers.Default).launch {
                    mapItemViewModel.searchKakaoMapItem(s.toString())
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // x버튼 리스너
        cancelBtn.setOnClickListener {
            inputSpace.setText("")
        }
    }
}


