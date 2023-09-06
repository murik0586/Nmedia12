package ru.netology.nmedia.repository


import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import ru.netology.nmedia.dto.Post
import java.io.IOException
import java.lang.Exception
import java.util.concurrent.TimeUnit

class PostRepositoryImpl : PostRepository {

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()
    private val gson = Gson()
    private val typeToken = object : TypeToken<List<Post>>() {}

    companion object {
        private const val BASE_URL = "http://10.0.2.2:9999"
        private val jsonType = "application/json".toMediaType()
    }

    override fun getAllAsync(callback: PostRepository.GetAllCallback) {
        val request: Request = Request.Builder()
            .url("${BASE_URL}/api/slow/posts")
            .build()

        client.newCall(request)
            .enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    val body = response.body?.string() ?: throw RuntimeException("body is null")
                    try {
                        callback.onSuccess(gson.fromJson(body, typeToken.type))
                    } catch (e: Exception) {
                        callback.onError(e)
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }
            })
    }

    override fun save(post: Post, callback: PostRepository.SaveCallback) {
        val request : Request = Request.Builder()
            .post(gson.toJson(post).toRequestBody(jsonType))
            .url("${BASE_URL}/api/posts")
            .build()

        client.newCall(request)
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }

                override fun onResponse(call: Call, response: Response) {
                }

            })
    }

    override fun likeById(id: Long) : Post {
        val requestLike:Request = Request.Builder()
            .post(gson.toJson("").toRequestBody(jsonType))
            .url("${BASE_URL}/api/posts/$id/likes")
            .build()

        return client.newCall(requestLike)
            .execute()
            .let {it.body?.string() ?: throw RuntimeException("body is null")}
            .let {
                gson.fromJson(it, Post::class.java)
            }
    }

    override fun unlikeById(id: Long) : Post {
        val requestUnlike:Request = Request.Builder()
            .delete()
            .url("${BASE_URL}/api/posts/$id/likes")
            .build()

        return client.newCall(requestUnlike)
            .execute()
            .let {it.body?.string() ?: throw RuntimeException("body is null")}
            .let {
                gson.fromJson(it, Post::class.java)
            }
    }

    override fun shareById(id: Long) {
    }

    override fun watchById(id: Long) {
    }




    override fun removeById(id: Long) {
        val request:Request = Request.Builder()
            .delete()
            .url("${BASE_URL}/api/posts/$id")
            .build()

        client.newCall(request)
            .execute()
            .close()
    }

    override fun getById(id: Long): Post {
        val request: Request = Request.Builder()
            .url("${BASE_URL}/api/posts/$id")
            .build()

        return client.newCall(request)
            .execute()
            .let {it.body?.string() ?: throw RuntimeException("body is null")}
            .let {
                gson.fromJson(it, Post::class.java)
            }
    }

}