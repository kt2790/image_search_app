package com.example.imagesearchapi.ui.fragment

import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imagesearchapi.data.ApiState
import com.example.imagesearchapi.databinding.FragmentSearchBinding
import com.example.imagesearchapi.model.ImageSearchResponse
import com.example.imagesearchapi.ui.adapter.SearchListAdapter


class SearchFragment : Fragment() {

    private lateinit var binding : FragmentSearchBinding
    private val viewModel: ContentViewModel by activityViewModels { ContentViewModelFactory() }
    private lateinit var recyclerViewState : Parcelable
    private val adapter by lazy {
        SearchListAdapter {
            viewModel.addBookmarkPref(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        binding.searchList.adapter = adapter
        binding.searchList.layoutManager = GridLayoutManager(requireActivity(), 2)

        binding.searchList.apply {
            itemAnimator = null
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val lastVisibleItemPosition =
                        (recyclerView.layoutManager as GridLayoutManager?)!!.findLastCompletelyVisibleItemPosition()
                    val itemTotalCount = recyclerView.adapter!!.itemCount
                    if (lastVisibleItemPosition >= itemTotalCount - 1) {
                        viewModel.getNextContent()
                    }
                }
            })
        }

        binding.confirmBtn.setOnClickListener {
            viewModel.getContentList(binding.searchText.text.toString(), "accuracy")
        }

        viewModel.searchResult.observe(viewLifecycleOwner) {
            when(it) {
                is UiState.Loading -> {}
                is UiState.Success -> {
                    val recyclerViewState = binding.searchList.layoutManager?.onSaveInstanceState()
                    adapter.setList(it.data?.toMutableList() ?: mutableListOf())
                    binding.searchList.layoutManager?.onRestoreInstanceState(recyclerViewState)
                }
                is UiState.Error -> {}
            }
        }
    }

}