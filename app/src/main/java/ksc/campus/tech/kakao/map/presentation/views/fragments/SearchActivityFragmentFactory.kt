package ksc.campus.tech.kakao.map.presentation.views.fragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import ksc.campus.tech.kakao.map.presentation.viewmodels.SearchActivityViewModel

class SearchActivityFragmentFactory(private val viewModel: SearchActivityViewModel) :
    FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return super.instantiate(classLoader, className)
    }
}