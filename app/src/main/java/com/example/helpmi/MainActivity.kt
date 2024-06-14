package com.example.helpmi


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button

import androidx.compose.material.Text
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val user = remember {
                mutableStateOf(User(0, ""))
            }
            val errorMessage = remember {
                mutableStateOf<String?>(null)
            }
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = "MainMenu") {
                composable("MainMenu") { MainMenuScreen(navController) }
                composable("LoginMenu"){LoginLayout(navController, user)}
                composable("registerMenu"){RegisterScreen(navController)}
                composable("HomeworkList"){HomeworkList(navController)}
                composable("HomeworkTopic"){ HomeworkTopic(navController) }

            }
        }


    }
}


@Composable
fun MainMenuScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { navController.navigate("LoginMenu") }) {
            Text(text = "Login")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate("registerMenu") }) {
            Text(text = "Register")
        }
    }
}

@Composable
fun HomeworkTopic(navController: NavController)
{
    AndroidView(factory = { context ->
        val view = LayoutInflater.from(context).inflate(R.layout.homework_topic, null)

        //TODO : Add logic to display the topic
        //TODO : Create functions for inserting comments
        view
    })

}

@Composable
fun HomeworkList(navController: NavController)
{
    AndroidView(factory = { context ->
        val view = LayoutInflater.from(context).inflate(R.layout.homework_list_menu, null)

        val recyclerView = view.findViewById<RecyclerView>(R.id.homeworkRecyclerView)

        recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = PostAdapter(navController, emptyList())
        HelpMi_api().fetchPosts(adapter)
        recyclerView.adapter = adapter

        view
    })

}



@Composable
fun RegisterScreen(navController: NavController) {
AndroidView(factory = { context ->

    val view = LayoutInflater.from(context).inflate(R.layout.register_layout, null)
    val registerButton = view.findViewById<Button>(R.id.registerButton)
    val usernameEditText = view.findViewById<EditText>(R.id.InputUsername)
    val passwordEditText = view.findViewById<EditText>(R.id.InputPassword)
    val emailEditText = view.findViewById<EditText>(R.id.InputEmail)

    registerButton.setOnClickListener(){
        val username = usernameEditText.text.toString()
        val password = passwordEditText.text.toString()
        val email = emailEditText.text.toString()
        Log.d("Register", "Username: $username, Password: $password, Email: $email")
        HelpMi_api().Register(username, password, email, context, navController)
    }
    view
})

}



@Composable
fun LoginLayout(navController: NavController, user: MutableState<User>){

    AndroidView(factory = { context ->
       val view = LayoutInflater.from(context).inflate(R.layout.login_layout, null)
        val registerButton = view.findViewById<Button>(R.id.registerButton)
        //can be used to regex the username and password; (TODO ?= undefined)
        val usernameEditText = view.findViewById<EditText>(R.id.InputUsername)
        val passwordEditText = view.findViewById<EditText>(R.id.InputPassword)

        registerButton.setOnClickListener(){
            navController.navigate("registerMenu")
        }

        val loginButton = view.findViewById<Button>(R.id.loginButton)
        loginButton.setOnClickListener(){
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()
            Log.d("Login", "Username: $username, Password: $password")
           HelpMi_api().Authenticate(username, password, user, context, navController)
      //TODO : add login logic
        }
        view
    })
    }

