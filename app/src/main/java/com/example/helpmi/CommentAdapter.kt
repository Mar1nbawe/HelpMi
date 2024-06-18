//package com.example.helpmi
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//import androidx.navigation.NavController
//import androidx.recyclerview.widget.RecyclerView
//
//class CommentAdapter(private val navController : NavController, private var postList: List<Post>) : RecyclerView.Adapter<CommentAdapter.PostViewHolder>() {
//
//    class CommentViewHolder(itemView: View, navController: NavController) : RecyclerView.ViewHolder(itemView) {
//        val title: TextView = itemView.findViewById(R.id.postTitle)
//        val content: TextView = itemView.findViewById(R.id.postContent)
//        val username: TextView = itemView.findViewById(R.id.postUsername)
//
//        init {
//            itemView.setOnClickListener {
//
//                navController.navigate("HomeworkTopic")
//            }
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.comment_item, parent, false)
//        return CommentViewHolder(view, navController)
//    }
//
//    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
//        val currentComment = CommentList[position]
//        holder.title.text = currentComment.title
//        holder.content.text = currentComment.content
//        holder.username.text = currentComment.username
//    }
//
//    override fun getItemCount() = CommentList.size
//
//    fun updateComments(newComments: List<Comment>) {
//        CommentList = newComments
//
//    }
//}