package com.example.instagramparse.fragments

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.example.instagramparse.LoginActivity
import com.example.instagramparse.MainActivity
import com.example.instagramparse.Post
import com.example.instagramparse.R
import com.parse.ParseFile
import com.parse.ParseUser
import java.io.File


class ComposeFragment : Fragment() {

    val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034
    val photoFileName = "photo.jpg"
    var photoFile: File? = null
    lateinit var imagePreview: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_compose, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imagePreview = view.findViewById(R.id.imagePost)

        val takePhotoButton = view.findViewById<Button>(R.id.takePhotoButton)
        val postButton = view.findViewById<Button>(R.id.postButton)
        val signOut = view.findViewById<Button>(R.id.signOUT)
        var progressBar = view.findViewById<ProgressBar>(R.id.progressBar)

        takePhotoButton.setOnClickListener() {
            onLaunchCamera()
        }

        postButton.setOnClickListener() {
            val description = view.findViewById<EditText>(R.id.postDescription).text.toString()
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
            if (resultCode == AppCompatActivity.RESULT_OK) {
                val takenImage = BitmapFactory.decodeFile(photoFile!!.absolutePath)

                imagePreview.setImageBitmap(takenImage)
            } else {
                Toast.makeText(requireContext(), "pic wasnt taken", Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun logOut() {
        ParseUser.logOut()
        val currentUser = ParseUser.getCurrentUser() // this will now be null
        if (currentUser == null) {
            goToLoginActivity()
        }
    }

    fun goToLoginActivity() {
        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }

    fun getPhotoFileUri(fileName: String): File {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        val mediaStorageDir =
            File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), MainActivity.TAG)

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(MainActivity.TAG, "failed to create directory")
        }

        // Return the file target for the photo based on filename
        return File(mediaStorageDir.path + File.separator + fileName)
    }

    fun submitPost(description: String, user: ParseUser, file: File, progressBar: ProgressBar) {
        val post = Post()
        post.setDescription(description)
        post.setUser(user)
        post.setImage(ParseFile(file))
        post.saveInBackground { exception ->
            if (exception != null) {
                Log.e(MainActivity.TAG, "ERROW HILE SAVING")
                exception.printStackTrace()
            } else {
                Log.i(MainActivity.TAG, "Succes saving post")
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
                FileProvider.getUriForFile(requireContext(), "com.codepath.fileprovider", photoFile!!)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

            // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
            // So as long as the result is not null, it's safe to use the intent.

            // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
            // So as long as the result is not null, it's safe to use the intent.
            if (intent.resolveActivity(requireContext().packageManager) != null) {
                // Start the image capture intent to take photo
                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE)
            }
        }
    }
}