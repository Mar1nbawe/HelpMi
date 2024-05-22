import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.helpmi.R

import com.example.helpmi.PostAdapter

class MainActivity : ComponentActivity() {
    private lateinit var postAdapter: PostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.homework_list_menu)

        val recyclerView = findViewById<RecyclerView>(R.id.homeworkRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        postAdapter = PostAdapter(emptyList())
        recyclerView.adapter = postAdapter

        // Fetch the data from the API and update the adapter
        // fetchPosts()
    }

    private fun fetchPosts() {
        // Fetch the data from the API
        // Once the data is fetched, update the adapter
        // postAdapter.updatePosts(fetchedPosts)
    }
}