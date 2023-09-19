package com.example.imagesearchapi.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.imagesearchapi.databinding.FragmentBookmarkBinding
import com.example.imagesearchapi.ui.adapter.BookmarkListAdapter

class BookmarkFragment : Fragment() {

    private lateinit var binding : FragmentBookmarkBinding
    private val viewModel: ContentViewModel by activityViewModels { ContentViewModelFactory()}
    private val adapter by lazy {
        BookmarkListAdapter {
            viewModel.deleteBookmarkPref(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookmarkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        binding.bookmarkList.adapter = adapter
        binding.bookmarkList.layoutManager = GridLayoutManager(requireActivity(), 2)

        viewModel.bookmarkList.observe(viewLifecycleOwner) {
            adapter.setList(it.toMutableList())
        }

    }


}