package com.example.blogapp

import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import android.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlogDetailsScreen(blog: BlogPost, navController: NavController){
    val decodedTitle = HtmlCompat.fromHtml(blog.title.rendered,HtmlCompat.FROM_HTML_MODE_COMPACT)
        .toString()
    val state = rememberScrollState()

    Column(modifier = Modifier
        .fillMaxSize()
        .background(androidx.compose.ui.graphics.Color.White)
    )
    {
        TopAppBar(
            title = {
                Text(text = "Blogs",
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    fontFamily = FontFamily.Serif)
                    },
            navigationIcon = {
                IconButton(onClick = { navController.navigate(Screen.BlogListScreen.route) }) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = colorResource(id = R.color.bg_color),
                titleContentColor = colorResource(id = R.color.title_color),
                navigationIconContentColor = colorResource(id = R.color.title_color)
            )
        )

        Column(
            modifier=Modifier.verticalScroll(
                state = state,
                enabled = true
            ).weight(1f),

        ) {
            Text(text = decodedTitle,fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.title_color),
                fontSize = 22.sp,modifier= Modifier
                    .padding(horizontal = 16.dp),
                textAlign = TextAlign.Justify,
                fontStyle = FontStyle.Italic
            )
            AndroidView(
                factory = { context -> TextView(context).apply {
                    setTextColor(Color.BLACK)
                    textSize = 18f
                    setLinkTextColor(Color.BLUE)
                    setLineSpacing(1.5f,1f)
                    setPadding(8,8,8,8)
                    movementMethod = LinkMovementMethod.getInstance()
                } },
                update = {
                    it.text = HtmlCompat.fromHtml(blog.content.rendered, HtmlCompat.FROM_HTML_MODE_COMPACT) },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            )
        }



    }
}
