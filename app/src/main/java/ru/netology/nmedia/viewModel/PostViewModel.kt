package ru.netology.nmedia.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryImpl
import ru.netology.nmedia.util.SingleLiveEvent
import java.io.IOException
import java.lang.Exception
import kotlin.concurrent.thread

private val empty = Post(
    id = 0L,
    content = "",
    author = "",
    published = "",
    authorAvatar = ""
)

class PostViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PostRepository = PostRepositoryImpl()
    private val _data = MutableLiveData(FeedModel())
    val data: LiveData<FeedModel>
        get() = _data
    private val edited = MutableLiveData(empty)
    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    init {
        loadPosts()
    }


    fun loadPosts() {
        _data.postValue(FeedModel(loading = true))

        repository.getAllAsync(object : PostRepository.GetAllCallback {
            override fun onError(e: Exception) {
                _data.postValue(FeedModel(error = true))
            }

            override fun onSuccess(posts: List<Post>) {
                _data.postValue(FeedModel(posts = posts, empty = posts.isEmpty()))
            }
        })
    }

    fun save() {
        edited.value?.let {
                repository.saveAsync(it, object : PostRepository.SaveCallback {
                    override fun onError(e: Exception) {
                        _data.postValue(FeedModel(error = true))
                    }

                    override fun onSuccess(post: Post) {
                        _data.postValue(FeedModel())
                        _postCreated.postValue(Unit)

                    }

                })
        }
        edited.value = empty

    }

    fun like(id : Long) {

        repository.getByIdAsync(id, object : PostRepository.GetByIdCallback {
            override fun onError(e: Exception) {
                _data.postValue(FeedModel(error = true))
            }

            override fun onSuccess(post: Post) {
                if (post.likedByMe) unlikeById(id) else likeById(id)
            }
        })



    }

    fun likeById(id: Long) {

            repository.likeByIdAsync(id, object : PostRepository.LikeCallback {
                override fun onError(e: Exception) {
                    _data.postValue(FeedModel(error = true))
                }

                override fun onSuccess(post: Post) {
                    _data.postValue(
                        FeedModel(posts =
                        _data.value!!.posts.map {
                            if (post.id == it.id)
                            {post.copy(likedByMe = post.likedByMe, likes = post.likes) }
                            else {
                                it
                            }
                        })
                    )
                }

            })



    }

    fun unlikeById(id : Long) {
        repository.unlikeByIdAsync(id, object : PostRepository.UnlikeCallback {
            override fun onError(e: Exception) {
               _data.postValue(FeedModel(error = true))
            }

            override fun onSuccess(post: Post) {
                _data.postValue(
                    FeedModel(posts =
                    _data.value!!.posts.map {
                        if (post.id == it.id)
                        {post.copy(likedByMe = post.likedByMe, likes = post.likes) }
                        else {
                            it
                        }
                    })
                )
            }

        })
    }

    fun removeById(id: Long) {
        val old = _data.value?.posts.orEmpty()

        repository.removeByIdAsync(id, object : PostRepository.RemoveByIdCallback {
            override fun onError(e: Exception) {
               _data.postValue(FeedModel(error = true))
            }

            override fun onSuccess() {
                try {
                    _data.postValue(
                        _data.value?.copy(posts = _data.value?.posts.orEmpty()
                            .filter { it.id != id })
                    )

                } catch (e: IOException) {
                    _data.postValue(_data.value?.copy(posts = old))
                }
            }

        })
    }



    fun edit(post: Post) {
        edited.value = post
    }

    fun changeContent(content: String) {
        var text = content.trim()
        if (edited.value?.content == text) {
            return
        }
        edited.value = edited.value?.copy(content = text)
    }

}