package com.example.instagramparse

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class PostAdapter(val context: Context, val posts: ArrayList<Post>) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostAdapter.ViewHolder, position: Int) {
        val post = posts.get(position)
        holder.bind(post)
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    fun clear() {
        posts.clear()
        notifyDataSetChanged()
    }

    // Add a list of items -- change to type used
    fun addAll(postList: List<Post>) {
        posts.addAll(postList)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemCreatedAt: TextView
        val itemUser: TextView
        val itemDescription: TextView
        val itemImage: ImageView

        init {
            itemUser = itemView.findViewById(R.id.itemUser)
            itemDescription = itemView.findViewById(R.id.itemDescription)
            itemImage = itemView.findViewById(R.id.itemImage)
            itemCreatedAt = itemView.findViewById(R.id.itemCreatedAt)
        }

        fun bind(post: Post) {
            itemUser.text = post.getUser()?.username
            itemDescription.text = post.getDescription()
            itemCreatedAt.text = post.getFormattedTimestamp(post.createdAt)

            Glide.with(itemView.context).load(post.getImage()?.url).into(itemImage)
        }
    }
}