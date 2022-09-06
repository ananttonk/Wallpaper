package com.example.wallpaper

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


object WallpaperService {

    const val API_KEY = "29654185-909a2a0fe68bde3f3424a9d9f"
    private const val BASE_URL = "https://pixabay.com/"

    val api = Retrofit.Builder().baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create()).build()
        .create(WallpaperGet::class.java)


    interface WallpaperGet {
        @GET("api/")
        fun getWallpaperList(@Query("key") Key:String= API_KEY,@Query("page") page:Int):Call<WallpaperList>
    }
}