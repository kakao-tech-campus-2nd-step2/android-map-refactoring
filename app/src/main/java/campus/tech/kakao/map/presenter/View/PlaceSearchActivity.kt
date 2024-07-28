package campus.tech.kakao.map.presenter.view

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout.VERTICAL
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import campus.tech.kakao.map.R
import campus.tech.kakao.map.presenter.view.adapter.FavoriteAdapter
import campus.tech.kakao.map.presenter.view.adapter.SearchResultAdapter
import campus.tech.kakao.map.presenter.viewModel.SearchViewModel
import campus.tech.kakao.map.databinding.ActivityPlaceSearchBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlaceSearchActivity : AppCompatActivity() {
    private val viewModel: SearchViewModel by viewModels()
    private lateinit var binding: ActivityPlaceSearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_search)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_place_search)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        settingSearchRecyclerView()
        settingFavoriteRecyclerView()
        setDeleteSearchListener()
    }

    private fun settingSearchRecyclerView() {
        setSearchAdapter()
        binding.searchResult.layoutManager = LinearLayoutManager(this)
        binding.searchResult.addItemDecoration(
            DividerItemDecoration(this, VERTICAL)
        )
    }

    private fun setSearchAdapter() {
        val adapter = SearchResultAdapter(
            onClickAdd = {
                viewModel.addFavorite(it)
                moveToMapActivity(it)
            })
        viewModel.currentResult.observe(this) {
            adapter.submitList(it)
        }
        binding.searchResult.adapter = adapter
    }

    private fun settingFavoriteRecyclerView() {
        setFavoriteAdapter()
        binding.favorite.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true)
    }

    private fun setFavoriteAdapter() {
        val adapter = FavoriteAdapter(
            onClickDelete = {
                viewModel.deleteFromFavorite(it)
            })

        binding.favorite.adapter = adapter
        viewModel.favoritePlace.observe(this) {
            adapter.submitList(it)
            binding.favorite.smoothScrollToPosition(maxOf(it.size-1,0))
        }
    }


    private fun setDeleteSearchListener() {
        binding.deleteSearch.setOnClickListener {
            binding.etSearchPlace.setText("")
        }
    }

    private fun moveToMapActivity(id:Int){
        val intent = Intent(this,MapActivity::class.java)
        intent.putExtra(INTENT_ID,id)
        setResult(RESULT_OK,intent)
        finish()
    }

    companion object{
        const val INTENT_ID = "id"
    }
}




















