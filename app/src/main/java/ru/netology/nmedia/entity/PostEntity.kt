package ru.netology.nmedia.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.dto.Post

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String,
    val published: String,
    val content: String,
    val likes: Int = 0,
    val shares: Int = 0,
    val watches: Int = 0,
    val likedByMe: Boolean = false,
    val sharedByMe: Boolean = false,
) {
    fun toDto() = Post(
        id,
        author,
        published,
        content,
        likes,
        shares,
        watches,
        likedByMe,
        sharedByMe
    )

    companion object {
        fun fromDto(dto:Post) =
            PostEntity(
                dto.id,
                dto.author,
                dto.published,
                dto.content,
                dto.likes,
                dto.shares,
                dto.watches,
                dto.likedByMe,
                dto.sharedByMe,
            )
    }
}
