package com.example.blogapp

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blogapp.ui.theme.apiService
import kotlinx.coroutines.launch
import java.lang.Exception

class BlogViewModel:ViewModel() {
    private val _blogs = mutableStateOf<List<BlogPost>>(emptyList())
    val blogs : State<List<BlogPost>> = _blogs

    private val _loading = mutableStateOf(false)
    val loading : State<Boolean> =_loading
    private var currentPage=1
    private var isLastPage = false

    init {
        loadBlogPosts()
    }

    fun loadBlogPosts() {
        if(isLastPage){
            return
        }
        _loading.value=true
        viewModelScope.launch {
            try {
                val response = apiService.getBlogPosts(perPage = 10,page=currentPage)
                if(response.isNotEmpty()){
                    _blogs.value+=response
                    currentPage++
                }else{
                    isLastPage=true
                }
                Log.i("CurrentPage",currentPage.toString())
            }
            catch (e:Exception){
               Log.i("Error","$e occurred while fetching blogs")
            }
            finally {
                _loading.value=false
            }
        }
    }
}