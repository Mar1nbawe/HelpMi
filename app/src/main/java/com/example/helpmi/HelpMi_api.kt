package com.example.helpmi

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import okhttp3.*
import org.json.JSONObject
import org.jsoup.Jsoup
import java.io.IOException

val api_link = "zmeurica.ddns.net"


val client = OkHttpClient()

fun Authenticate(username: String, password: String, user: MutableState<User>, context: Context){

    // Make sure to add .addPathSegment() for each "/" present in the link
    // Make sure to add .addQueryParameter() for each possible query present in the link
    //Always add a .scheme("http"), otherwise you'll get errors
    val urlRequest = HttpUrl.Builder().scheme("http").host(api_link)
        .addPathSegment("help_homework")
        .addPathSegment("login")
        .addQueryParameter("format", "true")
        .addQueryParameter("username",username)
        .addQueryParameter("password",password)
        .build()

    val request = Request.Builder().url(urlRequest).build()
    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            Log.d("Error", e.toString())
        }

        override fun onResponse(call: Call, response: Response) {
            response.body?.let {
                val responseObject = it.string()

                try{
                    //Jsoup will parse the html response into a document
                    val doc = Jsoup.parse(responseObject)
                    val jsonObject = JSONObject(doc.select("pre").text())

                    if(jsonObject.has("success")){
                        Log.d("Success!", "User Authenticated")
                        val id = jsonObject.getJSONObject("data").getInt("id")
                        val username = jsonObject.getJSONObject("data").getString("username")
                        user.value = User(id, username)

                    }
                    else if (jsonObject.has("error")){
                        val errorMessage = jsonObject.getString("error").toString()
                        Log.d("Error", errorMessage)

                        //State management will handle the error message with Toasts and other visual elements.
                        Handler(Looper.getMainLooper()).post {
                            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                        }
                    }
                        else{
                            Log.d("Error", "Unknown error")
                    }


                }
                catch (e: Exception){
                    Log.d("Error", e.toString())
                }


            }
        }
    })
}

fun Register (username: String, password: String, email: String, context: Context){
    val urlRequest = HttpUrl.Builder().scheme("http").host(api_link)
        .addPathSegment("help_homework")
        .addPathSegment("register")
        .addQueryParameter("format", "true")
        .addQueryParameter("username",username)
        .addQueryParameter("password",password)
        .addQueryParameter("email",email)
        .build()

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
                        Handler(Looper.getMainLooper()).post {
                            Toast.makeText(context, "User Registered", Toast.LENGTH_LONG).show()
                        }
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