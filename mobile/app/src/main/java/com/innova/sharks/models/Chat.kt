package com.innova.sharks.models

import com.google.firebase.database.IgnoreExtraProperties


@IgnoreExtraProperties
data class Chat(val username: String? = null, val message: String? = null, val timestamp: Long? = null) {
    // Null default values create a no-argument default constructor, which is needed
    // for deserialization from a DataSnapshot.
}