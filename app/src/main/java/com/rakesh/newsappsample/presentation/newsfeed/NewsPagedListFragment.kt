package com.rakesh.newsappsample.presentation.newsfeed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.rakesh.newsappsample.R
import com.rakesh.newsappsample.databinding.FragmentNewsPagedListBinding
import com.rakesh.newsappsample.domain.misc.Error
import com.rakesh.newsappsample.presentation.util.ResponseState
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class NewsPagedListFragment : Fragment(R.layout.fragment_news_paged_list), NewsListItemView.NewsItemClickListener {

    private var _binding: FragmentNewsPagedListBinding? = null
    private val binding get() = _binding!!
    private val newsViewModel by viewModels<NewsFeedViewModel>()
    private lateinit var newsPagedListAdapter: NewsPagedListAdapter

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

        newsPagedListAdapter = NewsPagedListAdapter(this)
        binding.recyclerViewNewsPagedList.adapter = newsPagedListAdapter

        initObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initObservers() {
        newsViewModel.getPaged2NewsFeed().loadFailState.observe(viewLifecycleOwner, {
            when (it) {
                is ResponseState.Loading -> showProgressLoading(it.loading)
                is ResponseState.Failure -> {
                    showProgressLoading(false)
                    if (it.error is Error.NoConnectivity) {
                        Toast.makeText(context, "Network is not available", Toast.LENGTH_SHORT).show()
                    }
                }
                is ResponseState.Success -> {
                    showProgressLoading(false)
                    binding.recyclerViewNewsPagedList.isVisible = true
                }
            }
        })

        newsViewModel.getPaged2NewsFeed().pagedList.observe(viewLifecycleOwner, {
            Timber.d("Result count = ${it.size}")
            binding.recyclerViewNewsPagedList.isVisible = true
            newsPagedListAdapter.submitList(it)
        })
    }

    private fun showProgressLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBarLoadingNewsPagedList.show()
            binding.progressBarLoadingNewsPagedList.isVisible = true
        } else {
            binding.progressBarLoadingNewsPagedList.hide()
            binding.progressBarLoadingNewsPagedList.isVisible = false
        }
    }

    override fun onNewsItemClicked(newsId: String) {

    }
}