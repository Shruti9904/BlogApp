package com.example.blogapp

sealed class Screen(val route:String){
    data object BlogListScreen : Screen("blogListScreen")
    data object BlogDetailsScreen : Screen("blogDetailsScreen")
}