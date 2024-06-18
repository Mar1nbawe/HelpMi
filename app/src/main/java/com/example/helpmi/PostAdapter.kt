package com.example.helpmi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView

class PostAdapter(private val navController : NavController, private var postList: List<Post>) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    class PostViewHolder(itemView: View, navController: NavController, postList: List<Post>) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.postTitle)
        val content: TextView = itemView.findViewById(R.id.postContent)
        val username: TextView = itemView.findViewById(R.id.postUsername)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val clickedPostId = postList[position].id
                    // Use 'clickedPostId' as needed
                    // For example, you can navigate to a new screen:
                    navController.navigate("homeworkTopic/$clickedPostId")
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.post_item, parent, false)
        return PostViewHolder(view, navController, postList)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val currentPost = postList[position]
        holder.title.text = currentPost.title
        holder.content.text = currentPost.content
        holder.username.text = currentPost.username
    }

    override fun getItemCount() = postList.size

    fun updatePosts(newPosts: List<Post>) {
        postList = newPosts

    }
}