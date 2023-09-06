package ru.netology.nmedia.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.db.AppDB
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryImpl

private val empty = Post(
    id = 0L,
    content = "",
    author = "",
    published = ""
)

class PostViewModel(application: Application) : AndroidViewModel(application) {
    private val repository : PostRepository = PostRepositoryImpl(
        AppDB.getInstance(context = application).postDao()
    )
    val data = repository.getAll()
    val edited = MutableLiveData(empty)
    fun likeById(id : Long) = repository.likeById(id)
    fun shareById(id : Long) = repository.shareById(id)
    fun watchById(id : Long) = repository.watchById(id)
    fun removeById(id : Long) = repository.removeById(id)

    fun save() {
        edited.value?.let {
            repository.save(it)
        }
        edited.value = empty
    }

    fun edit(post : Post){
        edited.value = post
    }

    fun changeContent(content : String) {
        var text = content.trim()
        if (edited.value?.content == text) {
            return
        }
        edited.value = edited.value?.copy(content = text)
    }
}