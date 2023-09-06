package ru.netology.nmedia.repository

import android.net.Uri
import androidx.lifecycle.LiveData
import ru.netology.nmedia.dto.Post

interface PostRepository {

    fun getAll() : List<Post>
    fun getById(id : Long) : Post
    fun likeById(id : Long) :Post
    fun unlikeById(id : Long) :Post
    fun shareById(id : Long)
    fun watchById(id : Long)
    fun save(post: Post)
    fun removeById(id: Long)
}