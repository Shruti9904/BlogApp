package com.example.blogapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.blogapp.ui.theme.BlogAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {

            BlogAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BlogApp()
                }
            }
        }
    }
}

@Composable
fun BlogApp(){
    val viewModel = viewModel<BlogViewModel>()
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.BlogListScreen.route){
        composable(route= Screen.BlogListScreen.route) {
            HomeScreen(viewModel = viewModel){
                navController.currentBackStackEntry?.savedStateHandle?.set("blog",it)
                navController.navigate(Screen.BlogDetailsScreen.route)
            }
        }
        composable(route = Screen.BlogDetailsScreen.route) {
            val blogPost=navController.previousBackStackEntry?.savedStateHandle?.get<BlogPost>("blog")
            if(blogPost!=null){
                BlogDetailsScreen(blog = blogPost,navController)
            }else{
                Box(modifier=Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(modifier=Modifier.align(Alignment.Center))
                }
            }
        }
    }
}
