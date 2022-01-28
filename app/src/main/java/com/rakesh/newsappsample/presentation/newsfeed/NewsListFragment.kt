package com.rakesh.newsappsample.presentation.newsfeed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.rakesh.newsappsample.R
import com.rakesh.newsappsample.databinding.FragmentNewsListBinding
import com.rakesh.newsappsample.domain.misc.Error
import com.rakesh.newsappsample.presentation.util.ResponseState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@AndroidEntryPoint
class NewsListFragment : Fragment(R.layout.fragment_news_list), NewsListItemView.NewsItemClickListener {


    private var _binding: FragmentNewsListBinding? = null
    private val binding get() = _binding!!

    private val newsViewModel by viewModels<NewsFeedViewModel>()
    private lateinit var newsListAdapter: NewsListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewsListBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObservers()

        newsListAdapter = NewsListAdapter(this)
        binding.recyclerViewNewsList.adapter = newsListAdapter

        //Live data
        //newsViewModel.fetchNewsFeedByPage(1, 25)

        //Flow
        newsViewModel.fetchNewsFeedByPageFlow(1, 25)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initObservers() {
        newsViewModel.newsFeedByPage.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseState.Loading -> showProgressLoading(it.loading)
                is ResponseState.Failure -> {
                    showProgressLoading(false)
                    if (it.error is Error.NoConnectivity) {
                        Toast.makeText(context, "Network is not available", Toast.LENGTH_SHORT).show()
                    }
                }
                is ResponseState.Success -> {
                    Timber.d("Result count = ${it.data.size}")
                    showProgressLoading(false)
                    binding.recyclerViewNewsList.isVisible = true

                    newsListAdapter.submitList(it.data)
                }
            }
        }

        newsViewModel.newsFeedByPageFlow.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach { state ->
                when (state) {
                    is ResponseState.Loading -> showProgressLoading(state.loading)
                    is ResponseState.Failure -> {
                        showProgressLoading(false)
                        if (state.error is Error.NoConnectivity) {
                            Toast.makeText(context, "Network is not available", Toast.LENGTH_SHORT).show()
                        }
                    }
                    is ResponseState.Success -> {
                        Timber.d("Result count = ${state.data.size}")
                        showProgressLoading(false)
                        binding.recyclerViewNewsList.isVisible = true

                        newsListAdapter.submitList(state.data)
                    }
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun showProgressLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBarLoadingNewsList.show()
            binding.progressBarLoadingNewsList.isVisible = true
        } else {
            binding.progressBarLoadingNewsList.hide()
            binding.progressBarLoadingNewsList.isVisible = false
        }
    }

    override fun onNewsItemClicked(newsId: String) {

    }
}