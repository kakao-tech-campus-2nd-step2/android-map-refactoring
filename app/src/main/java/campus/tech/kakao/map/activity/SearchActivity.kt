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
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.adapter.ItemClickListener
import campus.tech.kakao.map.Room.MapItemViewModel
import campus.tech.kakao.map.adapter.MapListAdapter
import campus.tech.kakao.map.R
import campus.tech.kakao.map.Room.MapItem
import campus.tech.kakao.map.adapter.SelectListAdapter
import campus.tech.kakao.map.databinding.ActivitySearchBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {
    private val mapItemViewModel: MapItemViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val binding : ActivitySearchBinding = DataBindingUtil.setContentView(this, R.layout.activity_search)
        binding.search = this

        //어댑터 설정
        val mapListAdapter = MapListAdapter(listOf(), LayoutInflater.from(this))
        val selectListAdapter = SelectListAdapter(listOf(), LayoutInflater.from(this))

        binding.mapList.adapter = mapListAdapter
        binding.mapList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        binding.selectList.adapter = selectListAdapter
        binding.selectList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        //리스너 정의
        mapListAdapter.setItemClickListener(object : ItemClickListener {
            override fun onClick(v: View, mapItem: MapItem) {
                mapItemViewModel.insertSelectItem(
                    MapItem(
                        0,
                        mapItem.name,
                        mapItem.address,
                        mapItem.category,
                        mapItem.x,
                        mapItem.y,
                        mapItem.kakaoId
                    )
                )
                val intent = Intent(this@SearchActivity, MapActivity::class.java)
                intent.putExtra("x", mapItem.x.toDouble())
                intent.putExtra("y", mapItem.y.toDouble())
                intent.putExtra("name", mapItem.name)
                intent.putExtra("address", mapItem.address)
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }
        })

        selectListAdapter.setCancelBtnClickListener(object : ItemClickListener {
            override fun onClick(v: View, selectItem: MapItem) {
                mapItemViewModel.deleteSelectItem(selectItem)
            }
        })

        selectListAdapter.setItemClickListener(object : ItemClickListener {
            override fun onClick(v: View, selectItem: MapItem) {
                binding.inputSpace.setText(selectItem.name)
            }
        })

        // 옵저버 설정
        mapItemViewModel.kakaoMapItemList.observe(this) {
            mapListAdapter.updateMapItemList(it)
            if (mapItemViewModel.kakaoMapItemList.value.isNullOrEmpty()) {
                binding.mainText.visibility = View.VISIBLE
            } else {
                binding.mainText.visibility = View.INVISIBLE
            }
        }

        mapItemViewModel.selectItemList.observe(this) {
            selectListAdapter.updateMapItemList(it)
        }

        // EditText 리스너
        binding.inputSpace.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                CoroutineScope(Dispatchers.Default).launch {
                    mapItemViewModel.searchKakaoMapItem(s.toString())
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // x버튼 리스너
        binding.cancelBtnInputSpace.setOnClickListener {
            binding.inputSpace.setText("")
        }
    }
}


