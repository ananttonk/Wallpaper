package com.example.wallpaper

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.wallpaper.databinding.ActivityWallpaperDetailBinding

class WallpaperDetail : AppCompatActivity() {
    lateinit var binding: ActivityWallpaperDetailBinding
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWallpaperDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.backBtn.setOnClickListener {
            onBackPressed()
        }
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        val image = intent.getStringExtra("image").orEmpty()
        Log.d("image123",image)
        Glide.with(this).load(image).into(binding.wallpaperImage)
    }
}