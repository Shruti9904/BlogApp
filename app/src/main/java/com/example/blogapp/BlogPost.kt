package com.example.blogapp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BlogPost(
    val id: Int,
    val title: Title,
    val content: Content,
    val date: String,
    val jetpack_featured_media_url: String
):Parcelable

@Parcelize
data class Title(val rendered:String):Parcelable

@Parcelize
data class Content(val rendered:String):Parcelable
