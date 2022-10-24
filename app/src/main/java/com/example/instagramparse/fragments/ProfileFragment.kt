package com.example.instagramparse.fragments

import android.util.Log
import com.example.instagramparse.Post
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery
import com.parse.ParseUser

class ProfileFragment : FeedFragment() {

    override fun queryPosts() {

        // Specify which class to query
        val query: ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)

        // Find all Post objects
        query.include(Post.keyUser)
        query.addDescendingOrder("createdAt")
        query.whereEqualTo(Post.keyUser, ParseUser.getCurrentUser())
        query.findInBackground(object: FindCallback<Post> {
            override fun done(posts: MutableList<Post>?, e: ParseException?) {
                if (e != null) {
                    Log.e(TAG, "ERROR")
                } else {
                    if (posts != null) {
                        for (post in posts) {
                            Log.i(
                                TAG, "Post: " + post.getDescription() + ", Username: " +
                                        post.getUser()?.username)
                        }
                        feedAdapter.clear()
                        allPosts.addAll(posts)
                        feedAdapter.notifyDataSetChanged()
                        swipeContainer.isRefreshing = false
                    }
                }
            }
        })
    }
}