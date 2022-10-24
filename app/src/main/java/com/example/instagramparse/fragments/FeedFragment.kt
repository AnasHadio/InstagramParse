package com.example.instagramparse.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.instagramparse.MainActivity
import com.example.instagramparse.Post
import com.example.instagramparse.PostAdapter
import com.example.instagramparse.R
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery

open class FeedFragment : Fragment() {

    lateinit var postRecyclerView: RecyclerView
    lateinit var feedAdapter: PostAdapter
    lateinit var swipeContainer: SwipeRefreshLayout
    var allPosts: MutableList<Post> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postRecyclerView = view.findViewById(R.id.postRecyclerView)

        feedAdapter = PostAdapter(requireContext(), allPosts as ArrayList<Post>)
        postRecyclerView.adapter = feedAdapter
        postRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        swipeContainer = view.findViewById(R.id.swipeContainer)

        swipeContainer.setOnRefreshListener {
            Log.i(MainActivity.TAG, "Refreshing Timeline")
            queryPosts()
        }

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light);

        queryPosts()
    }


    open fun queryPosts() {

        // Specify which class to query
        val query: ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)

        // Find all Post objects
        query.include(Post.keyUser)
        query.addDescendingOrder("createdAt")
        query.findInBackground(object: FindCallback<Post> {
            override fun done(posts: MutableList<Post>?, e: ParseException?) {
                if (e != null) {
                    Log.e(TAG, "ERROR")
                } else {
                    if (posts != null) {
                        for (post in posts) {
                            val createdAt = post.createdAt
                            Log.i(
                                TAG, "Post: " + post.getDescription() + ", Username: " +
                                    post.getUser()?.username + ", Time: " + post.getFormattedTimestamp(createdAt))
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

    companion object {
        const val TAG = "FeedFragment"
    }
}