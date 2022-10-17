package com.example.instagramparse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.parse.ParseUser

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Check if user is logged in, if so then go to MainActivity
        if (ParseUser.getCurrentUser() != null) {
            goToMainActivity()
        }

        val loginButton = findViewById<Button>(R.id.signIN)

        loginButton.setOnClickListener {
            Log.i(TAG, "clicked")

            val username = findViewById<EditText>(R.id.userName).text.toString()
            val password = findViewById<EditText>(R.id.passWord).text.toString()
            loginUser(username, password)
        }

        val signUpButton = findViewById<Button>(R.id.signUP)

        signUpButton.setOnClickListener {
            Log.i(TAG, "clicked up")

            val username = findViewById<EditText>(R.id.userName).text.toString()
            val password = findViewById<EditText>(R.id.passWord).text.toString()
            signUpUser(username, password)
        }
    }

    private fun signUpUser(username: String, password: String) {
        Log.i(TAG, "it's in here up")
        // Create the ParseUser
        val user = ParseUser()

        // Set fields for the user to be created
        user.setUsername(username)
        user.setPassword(password)

        user.signUpInBackground { e ->
            if (e == null) {
                // Hooray! Let them use the app now.
            } else {
                e.printStackTrace()
            }
        }
    }

    private fun loginUser(username: String, password: String) {
        Log.i(TAG, "it's in here")
        ParseUser.logInInBackground(username, password, ({ user, e ->
            Log.i(TAG, username)
            Log.i(TAG, password)
            if (user != null) {
                Log.i(TAG, "logied in")
                goToMainActivity()
            } else {
                e.printStackTrace()
                Toast.makeText(this, "error loggin", Toast.LENGTH_SHORT).show()
            }})
        )
    }

    private fun goToMainActivity() {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    companion object {
        val TAG = "LoginActivity"
    }
}