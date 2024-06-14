//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.example.helpmi.HelpMi_api
//import com.example.helpmi.R
//
//import android.content.Context
//import android.os.Handler
//import android.os.Looper
//import android.util.Log
//import android.widget.Toast
//import androidx.compose.runtime.MutableState
//import androidx.navigation.NavController
//import com.example.helpmi.Post
//import com.example.helpmi.PostAdapter
//import okhttp3.*
//import org.json.JSONObject
//import org.jsoup.Jsoup
//import java.io.IOException
//
//class MainActivity : ComponentActivity() {
//    private lateinit var postAdapter: PostAdapter
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.homework_list_menu)
//
//        val recyclerView = findViewById<RecyclerView>(R.id.homeworkRecyclerView)
//        recyclerView.layoutManager = LinearLayoutManager(this)
//        postAdapter = PostAdapter(emptyList())
//        recyclerView.adapter = postAdapter
//        HelpMi_api().fetchPosts(postAdapter)
//        // Fetch the data from the API and update the adapter
//        // fetchPosts()
//    }
//
//
//        // Fetch the data from the API
//        // Once the data is fetched, update the adapter
//        // postAdapter.updatePosts(fetchedPosts)
//    }
//
//
//
///*
//@Composable
//fun HomeworkList(navController: androidx.navigation.NavController)
//{
//AndroidView(factory = { context ->
//    val view = LayoutInflater.from(context).inflate(R.layout.homework_list_menu, null)
//
//    val recyclerView = view.findViewById<RecyclerView>(R.id.homeworkRecyclerView)
//
//    recyclerView.layoutManager = LinearLayoutManager(context)
//    val adapter = PostAdapter(emptyList())
//    recyclerView.adapter = adapter
//
//    view
//})
//
//}
// */