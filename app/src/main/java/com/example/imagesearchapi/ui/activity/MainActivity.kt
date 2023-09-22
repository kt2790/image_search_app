package com.example.imagesearchapi.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.imagesearchapi.R
import com.example.imagesearchapi.databinding.ActivityMainBinding
import com.example.imagesearchapi.ui.adapter.ViewPagerFragmentAdapter
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private val title by lazy {
        arrayOf("검색", "보관함")
    }
    private val vpAdapter by lazy {
        ViewPagerFragmentAdapter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        initView()
    }

    private fun initView() {
        binding.viewPager.adapter = vpAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = title[position]
        }.attach()
    }
}