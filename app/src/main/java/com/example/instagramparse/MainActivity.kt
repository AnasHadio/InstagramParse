package com.example.instagramparse

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.*
import androidx.core.content.FileProvider
import com.parse.*
import java.io.File


// Create posts by taking a photo with camera
class MainActivity : AppCompatActivity() {

    val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034
    val photoFileName = "photo.jpg"
    var photoFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        queryPosts()

        val takePhotoButton = findViewById<Button>(R.id.takePhotoButton)
        val postButton = findViewById<Button>(R.id.postButton)
        val signOut = findViewById<Button>(R.id.signOUT)
        var progressBar = findViewById<ProgressBar>(R.id.progressBar)


        takePhotoButton.setOnClickListener() {
            onLaunchCamera()
        }

        postButton.setOnClickListener() {
            val description = findViewById<EditText>(R.id.postDescription).text.toString()
            val user = ParseUser.getCurrentUser()
            progressBar.visibility = ProgressBar.VISIBLE
            if (photoFile != null) {
                submitPost(description, user, photoFile!!, progressBar)
            }
        }

        signOut.setOnClickListener() {
            logOut()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                val takenImage = BitmapFactory.decodeFile(photoFile!!.absolutePath)

                val imagePreview: ImageView = findViewById(R.id.imagePost)
                imagePreview.setImageBitmap(takenImage)
            } else {
                Toast.makeText(this, "pic wasnt taken", Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun goToLoginActivity() {
        val intent = Intent(this@MainActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun logOut() {
        ParseUser.logOut()
        val currentUser = ParseUser.getCurrentUser() // this will now be null
        if (currentUser == null) {
            goToLoginActivity()
        }
    }

    fun submitPost(description: String, user: ParseUser, file: File, progressBar: ProgressBar) {
        val post = Post()
        post.setDescription(description)
        post.setUser(user)
        post.setImage(ParseFile(file))
        post.saveInBackground { exception ->
            if (exception != null) {
                Log.e(TAG, "ERROW HILE SAVING")
                exception.printStackTrace()
            } else {
                Log.i(TAG, "Succes saving post")
                progressBar.visibility = ProgressBar.INVISIBLE
            }
        }
    }

    fun onLaunchCamera() {
        // create Intent to take a picture and return control to the calling application
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName)

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        if (photoFile != null) {
            val fileProvider: Uri =
                FileProvider.getUriForFile(this, "com.codepath.fileprovider", photoFile!!)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

            // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
            // So as long as the result is not null, it's safe to use the intent.

            // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
            // So as long as the result is not null, it's safe to use the intent.
            if (intent.resolveActivity(packageManager) != null) {
                // Start the image capture intent to take photo
                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE)
            }
        }
    }

    fun getPhotoFileUri(fileName: String): File {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        val mediaStorageDir =
            File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG)

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(TAG, "failed to create directory")
        }

        // Return the file target for the photo based on filename
        return File(mediaStorageDir.path + File.separator + fileName)
    }

    fun queryPosts() {

        // Specify which class to query
        val query: ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)

        // Find all Post objects
        query.include(Post.keyUser)
        query.findInBackground(object: FindCallback<Post> {
            override fun done(posts: MutableList<Post>?, e: ParseException?) {
                if (e != null) {
                    Log.e(TAG, "ERROR")
                } else {
                    if (posts != null) {
                        for (post in posts) {
                            Log.i(TAG, "Post: " + post.getDescription() + ", Username: " +
                                    post.getUser()?.username)
                        }
                    }
                }
            }

        })
    }

    companion object {
        val TAG = "MainActivity"
    }

}