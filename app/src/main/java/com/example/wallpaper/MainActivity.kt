package com.example.wallpaper

import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wallpaper.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: WallpaperAdapter
    private lateinit var layoutManager: GridLayoutManager
    private lateinit var viewModel: WallpaperViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolBar)
        val orientation = this.resources.configuration.orientation
        layoutManager = if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            GridLayoutManager(this, 3)
        } else {
            GridLayoutManager(this, 5)
        }
        setUpAdapter()
        viewModel = ViewModelProvider(this)[WallpaperViewModel::class.java]
        initWallpaperApi()
    }

    private fun setUpAdapter() {
        adapter = WallpaperAdapter(this)
        val recycleView = binding.recyclerviewWallpaper
        recycleView.layoutManager = layoutManager
        binding.recyclerviewWallpaper.adapter = adapter
        recycleView.addOnScrollListener(object :
            EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                viewModel.page += 2
                initWallpaperApi()
            }
        })
    }

    private fun initWallpaperApi() {
        viewModel.wallpaperResponse()
        viewModel.wallpaperList.observe(this) {
            adapter.update(it)
        }
    }
}