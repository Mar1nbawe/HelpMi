package com.example.helpmi


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import android.widget.Toolbar

import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.delay


class MainActivity : ComponentActivity() {
    var toolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            toolbar = findViewById(R.id.Toolbar)
            setActionBar(toolbar)

            val toolbarState = remember {
                mutableStateOf(toolbar)
            }
            val user = remember {
                mutableStateOf(User(0, ""))
            }

            val navController = rememberNavController()

            val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)


            NavHost(navController = navController, startDestination = if (sharedPreferences.getInt("id", 0) != 0) "HomeworkList" else "LoginMenu") {
                composable("LoginMenu") { LoginLayout(navController, user) }
                composable("registerMenu") { RegisterScreen(navController, user) }
                composable("HomeworkList") { HomeworkList(navController, user, toolbarState, sharedPreferences) }
                composable("HomeworkTopic/{postId}") { backStackEntry ->
                    HomeworkTopic(navController, backStackEntry.arguments?.getString("postId"), sharedPreferences.getInt("id", 0).toString())
                }
                composable("AddHomeworkScreen") { AddHomeworkScreen(navController) }
            }


        }
    }
}




@Composable
fun HomeworkTopic(navController: NavController, postId: String?, userId: String?)
{
    AndroidView(factory = { context ->
        val view = LayoutInflater.from(context).inflate(R.layout.homework_topic, null)



        //TODO : Add logic to display the topic
        //TODO : Create functions for inserting comments

//        val recyclerView = view.findViewById<RecyclerView>(R.id.homeworkRecyclerView)

//        recyclerView.layoutManager = LinearLayoutManager(context)
        val comments_recyclerView = view.findViewById<RecyclerView>(R.id.post_comments)
        comments_recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = CommentAdapter(navController, emptyList())
        HelpMi_api().fetchPostData(postId, view, adapter)
        comments_recyclerView.adapter = adapter

        val addCommentButton = view.findViewById<Button>(R.id.AddCommentButton)
        val inputComment = view.findViewById<EditText>(R.id.InputComment)

        addCommentButton.setOnClickListener {
            val comment = inputComment.text.toString()
            HelpMi_api().postComment(comment, postId, userId, view, adapter)
        }

        view
    })

}

@Composable
fun HomeworkList(navController: NavController, user: MutableState<User>, toolbar: MutableState<Toolbar?>, sharedPreferences: SharedPreferences)
{
    val context = LocalContext.current
    handleDoubleBackPress(onDoubleBackPress = {(context as? ComponentActivity)?.finish()} , context)

    AndroidView(factory = { context ->


        val view = LayoutInflater.from(context).inflate(R.layout.homework_list_menu, null)

         toolbar.value = view.findViewById(R.id.Toolbar)

        val recyclerView = view.findViewById<RecyclerView>(R.id.homeworkRecyclerView)
        val logOutButton = view.findViewById<LinearLayout>(R.id.logOutButton)
        val addHomeworkButton = view.findViewById<ImageButton>(R.id.addHomeworkButton)
        recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = PostAdapter(navController, emptyList())
        HelpMi_api().fetchPosts(adapter)
        recyclerView.adapter = adapter

        Log.d("HomeworkList", "User: ${user.value.username}")
        toolbar.value?.title = "Welcome, ${sharedPreferences.getString("username", "User")}"

        logOutButton.setOnClickListener(){
            navController.navigate("LoginMenu")
            user.value = User(0, "")
        }

        addHomeworkButton.setOnClickListener(){
            navController.navigate("AddHomeworkScreen")
        }

        view
    })

}




@Composable
fun AddHomeworkScreen(navController: NavController)
{
    AndroidView(factory = { context ->
        val view = LayoutInflater.from(context).inflate(R.layout.add_homework_screen, null)
        val titleEditText = view.findViewById<EditText>(R.id.InputTitle)
        val descriptionEditText = view.findViewById<EditText>(R.id.InputTopic)
        val submitButton = view.findViewById<Button>(R.id.CreateHomeworkButton)


        submitButton.setOnClickListener(){
            val title = titleEditText.text.toString()
            val description = descriptionEditText.text.toString()
            HelpMi_api().addPosts(title, description, context, navController)

        }

        view
    })
}



@Composable
fun RegisterScreen(navController: NavController, user: MutableState<User>) {
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
        HelpMi_api().Register(username, password, email, context, navController, user)
    }
    view
})

}



@Composable
fun LoginLayout(navController: NavController, user: MutableState<User>){

    AndroidView(factory = { context ->
       val view = LayoutInflater.from(context).inflate(R.layout.login_layout, null)
        val registerButton = view.findViewById<Button>(R.id.registerButton)

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

@Composable
fun handleDoubleBackPress(onDoubleBackPress: () -> Unit, context: Context) {
    var backPressCount by rememberSaveable { mutableStateOf(0) }

    LaunchedEffect(backPressCount) {
        if (backPressCount > 0) {
            delay(2000L)
            backPressCount = 0
        }
    }

    BackHandler {
        if (backPressCount++ > 0) {
            onDoubleBackPress()
        } else {
            Toast.makeText(context, "Press back again to exit", Toast.LENGTH_SHORT).show()
        }
    }
}


