package com.example.helpmi

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.navigation.NavController
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import org.jsoup.Jsoup
import java.io.IOException
import com.example.helpmi.User


class HelpMi_api {

    private val api_link = "zmeurica.ddns.net"

   val client = OkHttpClient()

    fun URLBuilder (scheme: String, pathSegments: List<String> = emptyList(), queryParameters: Map<String, String> = emptyMap()) : HttpUrl{

        val urlBuilder = HttpUrl.Builder().scheme(scheme).host(HelpMi_api().api_link)

        for (pathSegment in pathSegments){
            urlBuilder.addPathSegment(pathSegment)
        }

        for (queryParameter in queryParameters){
            urlBuilder.addQueryParameter(queryParameter.key, queryParameter.value)
        }

        return urlBuilder.build()

    }
    fun Authenticate(
        username: String,
        password: String,
        user: MutableState<User>,
        context: Context,
        navController: NavController,
    ) {

        // see URLBuilder function
        val pathSegment = listOf("help_homework", "login")
        val QueryParams = mapOf("format" to "true", "username" to username, "password" to password)
        val urlRequest = URLBuilder("http", pathSegment, QueryParams)


        val request = Request.Builder().url(urlRequest).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("Error", e.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.let {
                    val responseObject = it.string()

                    try {
                        //Jsoup will parse the html response into a document
                        val doc = Jsoup.parse(responseObject)
                        val jsonObject = JSONObject(doc.select("pre").text())

                        if (jsonObject.has("success")) {
                            Log.d("Success!", "User Authenticated")
                            val id = jsonObject.getJSONObject("data").getInt("id")
                            val username = jsonObject.getJSONObject("data").getString("username")
                            user.value = User(id, username)
                            //TODO add navigation on success
                            Handler(Looper.getMainLooper()).post {

                                Toast.makeText(context, "User Authenticated", Toast.LENGTH_LONG)
                                    .show()
                                navController.navigate("HomeworkList")

                            }
                        } else if (jsonObject.has("error")) {
                            val errorMessage = jsonObject.getString("error").toString()
                            Log.d("Error", errorMessage)

                            //State management will handle the error message with Toasts and other visual elements.
                            Handler(Looper.getMainLooper()).post {
                                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                            }
                        } else {
                            Log.d("Error", "Unknown error")
                        }


                    } catch (e: Exception) {
                        Log.d("Error", e.toString())
                    }


                }
            }
        })
    }


    fun Register(
        username: String,
        password: String,
        email: String,
        context: Context,
        navController: NavController,
        user: MutableState<User>
    ) {
        // see URLBuilder function
        val pathSegment = listOf("help_homework", "register")
        val QueryParams = mapOf("format" to "true", "username" to username, "password" to password, "email" to email)
        val urlRequest = URLBuilder("http", pathSegment, QueryParams)


        val request = Request.Builder().url(urlRequest).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("Error", e.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.let {
                    val responseObject = it.string()

                    try {
                        val doc = Jsoup.parse(responseObject)
                        val jsonObject = JSONObject(doc.select("pre").text())

                        if (jsonObject.has("success")) {
                            Log.d("Success!", "User Registered")
                            val id = jsonObject.getJSONObject("data").getInt("id")
                            val username = jsonObject.getJSONObject("data").getString("username")
                            user.value = User(id, username)
                            Handler(Looper.getMainLooper()).post {

                                Toast.makeText(context, "User Registered", Toast.LENGTH_LONG).show()
                                navController.navigate("HomeworkMenu")
                            }
                            // TODO add navigation on success
                        } else if (jsonObject.has("error")) {
                            val errorMessage = jsonObject.getString("error").toString()
                            Log.d("Error", errorMessage)

                            Handler(Looper.getMainLooper()).post {
                                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()

                            }
                        } else {
                            Log.d("Error", "Unknown error")
                        }
                    } catch (e: Exception) {
                        Log.d("Error", e.toString())
                    }
                }
            }
        })
    }


    fun fetchPosts(postAdapter: PostAdapter) {
        val api = URLBuilder("http", listOf("help_homework", "getPosts"), queryParameters = mapOf("format" to "true"))

        val request = Request.Builder().url(api).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("Error", e.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.let {
                    val responseObject = it.string()

                    if (responseObject.isBlank()) {
                        Log.d("Error", "Empty response from server")
                        return
                    }

                    try {
                        val doc = Jsoup.parse(responseObject)
                        val jsonArray = JSONArray(doc.select("pre").text())

                        val fetchedPosts : List<Post> = List(jsonArray.length()) { i ->
                            val post = jsonArray.getJSONObject(i)
                            Post(post.getInt("id"), post.getString("title"), post.getString("content"), post.getString("username"), post.getString("posted_at"))
                        }
                        Log.d("Posts", fetchedPosts[1].title)
                        postAdapter.updatePosts(fetchedPosts)
                        postAdapter.notifyDataSetChanged()
                    } catch (e: Exception) {
                        Log.d("Error", "Parsing error: ${e.message}")
                    }
                }
            }
        })
    }

    fun addPosts(title: String, content: String, user_id: Int, context: Context, navController: NavController ){

        val pathSegment = listOf("help_homework", "addPost")
        val QueryParams = mapOf("format" to "true", "user_id" to user_id.toString(), "title" to title, "content" to content)
        val urlRequest = URLBuilder("http", pathSegment, QueryParams)


        val request = Request.Builder().url(urlRequest).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("Error", e.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(context, "Homework added", Toast.LENGTH_LONG).show()
                        navController.navigate("HomeworkList")

                    }
                } else {
                    Log.d("Error", "Failed to add post: ${response.code}")
                }
            }
        })
    }
}

//TODO: Add a function to fetch the posts from the API