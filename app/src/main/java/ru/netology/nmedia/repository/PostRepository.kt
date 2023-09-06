package ru.netology.nmedia.repository

import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.SingleLiveEvent
import java.lang.Exception


interface PostRepository {
    fun getAllAsync(callback: GetAllCallback)
    fun getById(id : Long) : Post
    fun likeById(id : Long) :Post
    fun unlikeById(id : Long) :Post
    fun shareById(id : Long)
    fun watchById(id : Long)
    fun save(post : Post, callback: SaveCallback)
    fun removeById(id: Long)

    interface GetAllCallback {
        fun onError(e : Exception)
        fun onSuccess(posts : List<Post>)
    }

    interface SaveCallback {
        fun onError(e : Exception)
        fun onSuccess()
    }
}