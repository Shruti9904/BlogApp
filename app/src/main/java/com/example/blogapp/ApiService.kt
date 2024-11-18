package com.example.blogapp.ui.theme

import com.example.blogapp.BlogPost
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private val retrofit=Retrofit.Builder().baseUrl("https://blog.vrid.in/").
            addConverterFactory(GsonConverterFactory.create()).build()

interface ApiService {
    @GET("wp-json/wp/v2/posts")
    suspend fun getBlogPosts(@Query("per_page") perPage: Int, @Query("page") page: Int):List<BlogPost>
}

val apiService = retrofit.create(ApiService::class.java)
