package com.example.blogapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "blog_table")
data class BlogEntity(
    @PrimaryKey val id :Int,
    val title:String,
    val content :String,
    val date:String,
    val imageUrl:String
)
