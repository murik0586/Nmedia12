package ru.netology.nmedia.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.db.AppDB
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
                repository.save(it, object : PostRepository.SaveCallback {
                    override fun onError(e: Exception) {
                        _data.postValue(FeedModel(error =true))
                    }

                    override fun onSuccess() {
                        _postCreated.postValue(Unit)
                    }

                })
        }
        edited.value = empty
    }

    fun likeById(id: Long) {
        thread {
            val post = repository.getById(id)
            val updatedPost = if (post.likedByMe) repository.unlikeById(id) else repository.likeById(id)
            _data.postValue(
                FeedModel(posts =
                _data.value!!.posts.map {
                    if (post.id == it.id) updatedPost else it
                })
            )

        }
    }

    //fun shareById(id : Long) = repository.shareById(id)
    fun watchById(id: Long) = repository.watchById(id)
    fun removeById(id: Long) {
        thread {
            val old = _data.value?.posts.orEmpty()
            _data.postValue(
                _data.value?.copy(posts = _data.value?.posts.orEmpty()
                    .filter { it.id != id })
            )
            try {
                repository.removeById(id)
            } catch (e: IOException) {
                _data.postValue((_data.value?.copy(posts = old)))
            }
        }
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

    fun getById(id: Long) {
        thread {
            repository.getById(id)
        }
    }
}