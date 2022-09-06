package com.example.wallpaper

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wallpaper.databinding.WallpaperItemBinding

class WallpaperAdapter(private val context: Context) :
    RecyclerView.Adapter<WallpaperAdapter.ViewHolder>() {
    private val list = mutableListOf<WallpaperList.Hit>()

    inner class ViewHolder(val binding: WallpaperItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = WallpaperItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       list[position].apply {
           Glide.with(context).load(webformatURL).centerCrop().into(holder.binding.itemImage)
           holder.itemView.setOnClickListener {
               val intent = Intent(context, WallpaperDetail::class.java)
               intent.putExtra("image", largeImageURL)
               context.startActivity(intent)
           }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(it: WallpaperList) {
        list.addAll(it.hits)
        this@WallpaperAdapter.notifyDataSetChanged()
    }
}