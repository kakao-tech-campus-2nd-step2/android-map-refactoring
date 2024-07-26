package campus.tech.kakao.map.presenter.View

import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout.VERTICAL
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.R
import campus.tech.kakao.map.domain.VO.Place
import campus.tech.kakao.map.presenter.View.adapter.FavoriteAdapter
import campus.tech.kakao.map.presenter.View.adapter.SearchResultAdapter
import campus.tech.kakao.map.ViewModel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PlaceSearchActivity : AppCompatActivity() {
    private val viewModel: SearchViewModel by viewModels()

    private lateinit var searchResult: RecyclerView
    private lateinit var noItem: TextView
    private lateinit var etSearchPlace: EditText
    private lateinit var deleteSearch: ImageView
    private lateinit var favorite: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_search)

        searchResult = findViewById<RecyclerView>(R.id.searchResult)
        etSearchPlace = findViewById<EditText>(R.id.etSearchPlace)
        noItem = findViewById<TextView>(R.id.noItem)
        deleteSearch = findViewById<ImageView>(R.id.deleteSearch)
        favorite = findViewById<RecyclerView>(R.id.favorite)

        settingSearchRecyclerView()
        settingFavoriteRecyclerView()
        setDeleteSearchListener()
        setEditTextListener()

    }

    private fun settingSearchRecyclerView() {
        setSearchAdapter()
        searchResult.layoutManager = LinearLayoutManager(this)
        searchResult.addItemDecoration(
            DividerItemDecoration(this, VERTICAL)
        )
    }

    private fun settingFavoriteRecyclerView() {
        setFavoriteAdapter()
        favorite.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true)
    }

    private fun setFavoriteAdapter() {
        val adapter = FavoriteAdapter(
            onClickDelete = {
                viewModel.deleteFromFavorite(it)
            })

        favorite.adapter = adapter
        viewModel.favoritePlace.observe(this) {
            adapter.submitList(it)
            favorite.smoothScrollToPosition(maxOf(it.size-1,0))
        }
    }


    private fun setDeleteSearchListener() {
        deleteSearch.setOnClickListener {
            etSearchPlace.setText("")
        }
    }

    private fun setEditTextListener() {
        etSearchPlace.addTextChangedListener {
            CoroutineScope(Dispatchers.IO).launch {
                viewModel.searchPlaceRemote(etSearchPlace.text.toString())
            }
        }
    }

    private fun setSearchAdapter() {
        val adapter = SearchResultAdapter(
            onClickAdd = {
                viewModel.addFavorite(it)
                moveToMapActivity(it)
            })
        viewModel.currentResult.observe(this) {
            adapter.submitList(it)
            handleVisibility(it)
        }
        searchResult.adapter = adapter
    }

    private fun moveToMapActivity(id:Int){
        val intent = Intent(this,MapActivity::class.java)
        intent.putExtra(INTENT_ID,id)
        setResult(RESULT_OK,intent)
        finish()
    }

    private fun handleVisibility(places : List<Place>){
        if(places.isEmpty()){
            searchResult.visibility = GONE
            noItem.visibility = VISIBLE
        } else {
            searchResult.visibility = VISIBLE
            noItem.visibility = GONE
        }
    }

    companion object{
        const val INTENT_ID = "id"
    }
}




















