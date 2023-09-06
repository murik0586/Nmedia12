package ru.netology.nmedia.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.card_post.*
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.dto.WallService

interface OnInteractionListener {
    fun onLike(post: Post) {}
    fun onShare(post: Post) {}
    fun onWatch(post: Post) {}
    fun onEdit(post: Post) {}
    fun onRemove(post: Post) {}
    fun onPlayVideo(post: Post) {}
}

class PostAdapter (
    private val onInteractionListener: OnInteractionListener
) : ListAdapter<Post, PostViewHolder>(PostViewHolder.PostDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, onInteractionListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }

}

class PostViewHolder(
    private val binding: CardPostBinding,
    private val onInteractionListener: OnInteractionListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post) {
        binding.apply {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            imgbLiked.text = WallService.displayCount(post.likes)
            imgbShare.text = WallService.displayCount(post.shares)
            imgbWatch.text = WallService.displayCount(post.watches)
            imgbLiked.isChecked = post.likedByMe
            imgbShare.isChecked = post.sharedByMe
            videoView.setVideoURI(Uri.parse(post.videoUrl))

            if (post.videoUrl.isNotEmpty()) {
                videoLayout.visibility = View.VISIBLE

                videoView.apply {
                   requestFocus()
                   start()
                }
                } else {
                    videoLayout.visibility = View.GONE
                }

                videoLayout.setOnClickListener {
                    onInteractionListener.onPlayVideo(post)
                }


                imgbLiked.setOnClickListener {
                    onInteractionListener.onLike(post)
                }

                imgbShare.setOnClickListener {
                    onInteractionListener.onShare(post)
                }

                imgbWatch.setOnClickListener {
                    onInteractionListener.onWatch(post)
                }



                menu.setOnClickListener {
                    PopupMenu(it.context, it).apply {
                        inflate(R.menu.options_post)
                        setOnMenuItemClickListener { item ->
                            when (item.itemId) {
                                R.id.remove -> {
                                    onInteractionListener.onRemove(post)
                                    true
                                }

                                R.id.edit -> {
                                    onInteractionListener.onEdit(post)
                                    true
                                }

                                else -> false
                            }
                        }
                    }.show()
                }
            }


        }



    class PostDiffCallback : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem == newItem
        }

    }
}
