package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.entity.PostEntity

class PostRepositoryImpl(
    private val dao: PostDao
) : PostRepository {

    override fun getAll() = Transformations.map(dao.getAll()) {
        list ->
        list.map {
            Post(
                it.id,
                it.author,
                it.published,
                it.content,
                it.likes,
                it.shares,
                it.watches,
                it.likedByMe,
                it.sharedByMe,
                it.videoUrl
            )
        }
    }

    override fun likeById(id: Long) {
        dao.likeById(id)
    }

    override fun shareById(id: Long) {
        dao.shareById(id)
    }

    override fun watchById(id: Long) {
        dao.watchById(id)
    }

    override fun save(post: Post) {
        dao.save(PostEntity.fromDto(post))
    }


    override fun removeById(id: Long) {
        dao.removeById(id)
    }
}