package com.example.blogapp

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.fontResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.HtmlCompat
import coil.compose.rememberAsyncImagePainter
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun HomeScreen(viewModel: BlogViewModel,navToDetails:(BlogPost)->Unit){
    Box(modifier= Modifier
        .fillMaxSize()
        .background(colorResource(id = R.color.bg_color))
    ) {
        if(viewModel.loading.value){
            CircularProgressIndicator(modifier=Modifier.align(Alignment.Center))
        }else{
            BlogListScreen(blogs = viewModel.blogs.value,navToDetails,viewModel)
            Log.d("BlogScreen", "Blogs: ${viewModel.blogs.value}")
        }
    }
}

@Composable
fun BlogListScreen(blogs: List<BlogPost>, navToDetails: (BlogPost) -> Unit,viewModel: BlogViewModel){
    val listState = rememberLazyListState()

    val isAtEndOfList = remember {
        derivedStateOf {
            val visibleItems = listState.layoutInfo.visibleItemsInfo
            val lastVisibleIndex = visibleItems.lastOrNull()?.index
            lastVisibleIndex != null && lastVisibleIndex == blogs.size - 1
        }
    }

    LaunchedEffect(isAtEndOfList.value) {
        if (isAtEndOfList.value && !viewModel.loading.value) {
            viewModel.loadBlogPosts()
        }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Text(
            text = "Vrid Blogs",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(id = R.color.title_color),
            fontFamily = FontFamily.Serif
        )

        LazyColumn(state = listState){
            items(blogs){blog->
                BlogPostItem(blog,navToDetails)
                Log.i("title",blog.title.rendered)
            }
        }
    }

}

@Composable
fun BlogPostItem(blog: BlogPost, navToDetails: (BlogPost) -> Unit){
    val decodedTitle = HtmlCompat.fromHtml(blog.title.rendered,HtmlCompat.FROM_HTML_MODE_LEGACY).toString()

    Card (modifier= Modifier
        .fillMaxWidth()
        .height(180.dp)
        .padding(vertical = 8.dp)
        .clickable {
            navToDetails(blog)
            Log.i("click:", "$blog")
        }, elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp, pressedElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ){
        Row(verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth())
        {
            Image(painter = rememberAsyncImagePainter(model = blog.jetpack_featured_media_url),
                contentDescription = "Blog",
                modifier = Modifier
                    .size(150.dp)
                    .fillMaxHeight(1f)
                    .padding(4.dp)
                    .clip(RoundedCornerShape(20.dp)),
                contentScale = ContentScale.FillHeight
                )
            Column(horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.SpaceAround,
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = 8.dp)
                    .weight(1f)
            ) {
                Text(
                    text =decodedTitle,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontSize = 16.sp
                )
                Text(
                    text = formatDate(blog.date)?:"",
                    color = Color.DarkGray,
                    fontSize = 14.sp
                )
            }
        }
    }
}


fun formatDate(date:String): String? {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    val outputFormat = SimpleDateFormat("MMM dd,yyyy",Locale.getDefault())
    val formattedDate=inputFormat.parse(date)?.let {
        outputFormat.format(it)
    }
    return formattedDate
}
