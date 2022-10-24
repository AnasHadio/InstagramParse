package com.example.instagramparse

import com.parse.ParseClassName
import com.parse.ParseFile
import com.parse.ParseObject
import com.parse.ParseUser
import java.util.*

@ParseClassName("Post")
class Post : ParseObject() {

    fun getDescription() : String? {
        return getString(keyDescription)
    }

    fun setDescription(description: String) {
        put(keyDescription, description)
    }

    fun getImage() : ParseFile? {
        return getParseFile(keyImage)
    }

    fun setImage(parsefile: ParseFile) {
        put(keyImage, parsefile)
    }

    fun getUser() : ParseUser? {
        return getParseUser(keyUser)
    }

    fun setUser(user: ParseUser) {
        put(keyUser, user)
    }

    fun getFormattedTimestamp(createdAt: Date): String? {
        return TimeFormatter.getTimeDifference(createdAt.toString())
    }

    companion object {
        const val keyDescription = "description"
        const val keyImage = "image"
        const val keyUser = "user"
    }
}