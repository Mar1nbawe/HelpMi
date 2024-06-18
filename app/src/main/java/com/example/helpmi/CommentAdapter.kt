package com.example.helpmi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView

class CommentAdapter(private val navController : NavController, private var CommentList: List<Comment>) : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    class CommentViewHolder(itemView: View, navController: NavController, CommentList: List<Comment>) : RecyclerView.ViewHolder(itemView) {
        val username: TextView = itemView.findViewById(R.id.comment_username)
        val content: TextView = itemView.findViewById(R.id.comment_content)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.comment_item, parent, false)
        return CommentViewHolder(view, navController, CommentList)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val currentComment = CommentList[position]
        holder.username.text = currentComment.username
        holder.content.text = currentComment.content
    }

    override fun getItemCount() = CommentList.size

    fun updateComments(newComments: List<Comment>) {
        CommentList = newComments
    }
}