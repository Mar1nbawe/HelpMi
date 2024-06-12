import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.helpmi.HelpMi_api
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
        val api = HelpMi_api()


        // Fetch the data from the API
        // Once the data is fetched, update the adapter
        // postAdapter.updatePosts(fetchedPosts)
    }
}


/*
@Composable
fun HomeworkList(navController: androidx.navigation.NavController)
{
AndroidView(factory = { context ->
    val view = LayoutInflater.from(context).inflate(R.layout.homework_list_menu, null)

    val recyclerView = view.findViewById<RecyclerView>(R.id.homeworkRecyclerView)

    recyclerView.layoutManager = LinearLayoutManager(context)
    val adapter = PostAdapter(emptyList())
    recyclerView.adapter = adapter

    view
})

}
 */