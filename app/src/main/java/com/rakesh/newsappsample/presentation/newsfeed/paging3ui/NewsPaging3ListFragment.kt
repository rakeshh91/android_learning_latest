package com.rakesh.newsappsample.presentation.newsfeed.paging3ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.rakesh.newsappsample.R
import com.rakesh.newsappsample.databinding.FragmentNewsPagedListBinding
import com.rakesh.newsappsample.presentation.newsfeed.NewsFeedViewModel
import com.rakesh.newsappsample.presentation.newsfeed.NewsListItemView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter

@AndroidEntryPoint
class NewsPaging3ListFragment : Fragment(R.layout.fragment_news_paged_list), NewsListItemView.NewsItemClickListener {

    private var _binding: FragmentNewsPagedListBinding? = null
    private val binding get() = _binding!!
    private val newsViewModel by viewModels<NewsFeedViewModel>()

    private lateinit var newsPaging3Adapter: NewsPaging3Adapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewsPagedListBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        newsPaging3Adapter = NewsPaging3Adapter(this)
        binding.recyclerViewNewsPagedList.adapter = newsPaging3Adapter.withLoadStateHeaderAndFooter(
            header = PostsLoadStateAdapter(newsPaging3Adapter),
            footer = PostsLoadStateAdapter(newsPaging3Adapter)
        )

        initObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initObservers() {
        lifecycleScope.launchWhenCreated {
            newsViewModel.getPaged3NewsFeed().collectLatest {
                binding.recyclerViewNewsPagedList.isVisible = true
                newsPaging3Adapter.submitData(it)
            }
        }

        lifecycleScope.launchWhenCreated {
            newsPaging3Adapter.loadStateFlow
                // Use a state-machine to track LoadStates such that we only transition to
                // NotLoading from a RemoteMediator load if it was also presented to UI.
                .asMergedLoadStates()
                // Only emit when REFRESH changes, as we only want to react on loads replacing the
                // list.
                .distinctUntilChangedBy { it.refresh }
                // Only react to cases where REFRESH completes i.e., NotLoading.
                .filter { it.refresh is LoadState.NotLoading }
                // Scroll to top is synchronous with UI updates, even if remote load was triggered.
                .collect { binding.recyclerViewNewsPagedList.scrollToPosition(0) }
        }
    }

    override fun onNewsItemClicked(newsId: String) {

    }
}