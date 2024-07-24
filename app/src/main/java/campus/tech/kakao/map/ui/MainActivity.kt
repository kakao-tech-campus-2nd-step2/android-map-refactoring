package campus.tech.kakao.map.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import campus.tech.kakao.map.R
import campus.tech.kakao.map.adapter.Adapter
import campus.tech.kakao.map.data.AppDatabase
import campus.tech.kakao.map.data.Profile
import campus.tech.kakao.map.databinding.ActivityMainBinding
import campus.tech.kakao.map.network.Document
import campus.tech.kakao.map.network.KakaoResponse
import campus.tech.kakao.map.network.Network
import campus.tech.kakao.map.network.SearchService
import campus.tech.kakao.map.utility.CategoryGroupCode
import campus.tech.kakao.map.utility.SaveHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.concurrent.thread

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var network: Network

    @Inject
    lateinit var searchService: SearchService

    @Inject
    lateinit var searchSave: SaveHelper

    lateinit var binding: ActivityMainBinding
    lateinit var adapter: Adapter
    lateinit var tvNoResult: TextView
//    lateinit var llSave: LinearLayoutCompat
//    lateinit var hScrollView: HorizontalScrollView
    lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "profiles"
        ).fallbackToDestructiveMigration().build()

        tvNoResult = findViewById(R.id.tvNoResult)

        adapter = Adapter(mutableListOf())
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val search = s.toString()
                if (search.isEmpty()) {
                    showNoResults()
                } else {
                    lifecycleScope.launch {
                        try {
                            val result = withContext(Dispatchers.IO) { searchService.searchKeyword(search) }
                            searchProfiles(result)
                        } catch (error:Exception) {
                            Toast.makeText(this@MainActivity, "요청 실패: ${error.message}", Toast.LENGTH_SHORT).show()
                        }
                    } // 키워드
                    CategoryGroupCode.categoryMap[search]?.let { categoryCode ->
                        lifecycleScope.launch {
                            try {
                                val result = searchService.searchCategory(categoryCode)
                                searchProfiles(result)
                            } catch (error: Exception) {
                                Toast.makeText(this@MainActivity, "요청 실패: ${error.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }  // 카테고리
                }
            }
        })

        adapter.setOnItemClickListener(object : Adapter.OnItemClickListener {
            override fun onItemClick(name: String, address: String, latitude: String, longitude: String) {
                if (searchSave.isProfileInSearchSave(name, binding.llSave)) {
                    searchSave.removeSavedItem(name, binding.llSave)
                }
                searchSave.addSavedItem(name, binding.llSave, binding.hScrollView, LayoutInflater.from(this@MainActivity), binding.etSearch)
                val intent = Intent(this@MainActivity, MapActivity::class.java).apply {
                    putExtra("name", name)
                    putExtra("address", address)
                    putExtra("latitude", latitude)
                    putExtra("longitude", longitude)
                }
                startActivity(intent)
            }
        })

        binding.btnClose.setOnClickListener {
            binding.etSearch.text?.clear()
        }
        searchSave.loadSavedItems(binding.llSave, binding.hScrollView, LayoutInflater.from(this), binding.etSearch)
    }

    fun searchProfiles(searchResult: KakaoResponse?) {
        searchResult?.documents?.let { documents ->
            if (documents.isEmpty()) {
                showNoResults()
            } else {
                val profiles = documents.map { it.toProfile() }
                adapter.updateProfiles(profiles)

                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        db.profileDao().insertAll(*profiles.toTypedArray())
                    }
                }

                binding.tvNoResult.visibility = View.GONE
            }
        } ?: showNoResults()
    }

    fun Document.toProfile(): Profile {
        return Profile(name = this.name, address = this.address, type = this.type, latitude = this.latitude, longitude = this.longitude)
    }

    fun showNoResults() {
        tvNoResult.visibility = View.VISIBLE
        adapter.updateProfiles(emptyList())
    }

    override fun onPause() {
        super.onPause()
        searchSave.saveSavedItems(binding.llSave)
    }
}
