package ru.netology.nmedia.repository

import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.SingleLiveEvent
import java.lang.Exception


interface PostRepository {
    fun getAllAsync(callback: GetAllCallback)
    fun getByIdAsync(id : Long, callback: GetByIdCallback)
    fun likeByIdAsync(id : Long, callback: LikeCallback)
    fun unlikeByIdAsync(id : Long, callback: UnlikeCallback)
    fun saveAsync(post : Post, callback: SaveCallback)
    fun removeByIdAsync(id: Long, callback: RemoveByIdCallback)

    interface GetAllCallback {
        fun onError(e : Exception)
        fun onSuccess(posts : List<Post>)
    }

    interface SaveCallback {
        fun onError(e : Exception)
        fun onSuccess(post: Post)
    }

    interface LikeCallback {
        fun onError(e : Exception)
        fun onSuccess(post: Post)
    }

    interface UnlikeCallback {
        fun onError(e : Exception)
        fun onSuccess(post: Post)
    }

    interface GetByIdCallback {
        fun onError(e : Exception)
        fun onSuccess(post: Post)
    }

    interface RemoveByIdCallback {
        fun onError(e : Exception)
        fun onSuccess()
    }
}