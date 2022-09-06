package com.example.wallpaper

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WallpaperViewModel():ViewModel() {
    var wallpaperList=MutableLiveData<WallpaperList>()
    var page=1

    fun wallpaperResponse(){
        val wallpaper = WallpaperService.api.getWallpaperList(page = page)
        wallpaper.enqueue(object : Callback<WallpaperList> {
            override fun onResponse(call: Call<WallpaperList>, response: Response<WallpaperList>) {
                response.body()?.let {
                    Log.d("success",it.toString())
                    if (it.hits.isNotEmpty()) {
                       wallpaperList.value=it
                    }
                }
            }
            override fun onFailure(call: Call<WallpaperList>, t: Throwable) {
                t.message?.let {
                    Log.d("failedApi", it)
                }
            }
        })
    }


}