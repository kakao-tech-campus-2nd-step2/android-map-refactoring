package ksc.campus.tech.kakao.map.presentation.views.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ksc.campus.tech.kakao.map.R
import ksc.campus.tech.kakao.map.domain.models.SearchResult
import ksc.campus.tech.kakao.map.presentation.viewmodels.SearchActivityViewModel
import ksc.campus.tech.kakao.map.presentation.views.adapters.SearchResultAdapter
import javax.inject.Inject

@AndroidEntryPoint
class SearchResultFragment @Inject constructor() :
    Fragment() {
    private val viewModel: SearchActivityViewModel by activityViewModels()
    private lateinit var searchResultRecyclerView: RecyclerView
    private lateinit var noResultHelpText: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search_result, container, false)
    }

    private fun setInitialValueToAdapter() {
        viewModel.searchResult.value.let {
            (searchResultRecyclerView.adapter as? SearchResultAdapter)?.submitList(it)
        }
    }

    private fun initiateRecyclerView(view: View) {
        searchResultRecyclerView = view.findViewById(R.id.list_search_result)
        searchResultRecyclerView.adapter =
            SearchResultAdapter { item: SearchResult, _: Int ->
                viewModel.clickSearchResultItem(item)
            }
        searchResultRecyclerView.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        searchResultRecyclerView.addItemDecoration(
            DividerItemDecoration(
                activity,
                DividerItemDecoration.VERTICAL
            )
        )
    }

    private fun initiateSearchResultLiveDataObservation() {
        viewLifecycleOwner.lifecycleScope.launch{
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.searchResult.collect{
                    (searchResultRecyclerView.adapter as? SearchResultAdapter)?.submitList(it)
                    setNoResultHelpTextActive(it.isEmpty())
                }
            }
        }
    }

    private fun setNoResultHelpTextActive(active: Boolean) {
        noResultHelpText.isVisible = active
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        noResultHelpText = view.findViewById(R.id.text_no_result)

        initiateRecyclerView(view)
        initiateSearchResultLiveDataObservation()
        setInitialValueToAdapter()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    viewModel.switchContent(SearchActivityViewModel.ContentType.MAP)
                }

            })
    }
}