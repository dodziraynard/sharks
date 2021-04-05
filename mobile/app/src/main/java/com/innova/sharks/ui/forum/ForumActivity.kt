package com.innova.sharks.ui.forum

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.innova.sharks.R
import com.innova.sharks.adapter.ChatAdapter
import com.innova.sharks.models.Chat
import com.innova.sharks.models.Lesson
import com.innova.sharks.ui.start.StartActivity
import com.innova.sharks.utils.Constants
import com.innova.sharks.utils.Constants.FORUM_LESSON
import com.innova.sharks.utils.Functions.getUsername
import kotlinx.android.synthetic.main.activity_forum.*
import java.util.*
import kotlin.collections.ArrayList

class ForumActivity : AppCompatActivity() {

    companion object {
        fun starter(context: Context, lesson: Lesson) {
            val intent = Intent(context, ForumActivity::class.java)
            intent.putExtra(FORUM_LESSON, lesson)
            intent.flags = Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT
            context.startActivity(intent)
        }

        private const val TAG = "ForumActivity"
    }

    private val chatAdapter: ChatAdapter = ChatAdapter(this@ForumActivity)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forum)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        // Get username or request login
        val username = getUsername(this@ForumActivity)
        if (username.isEmpty()) {
            val intent = Intent(this, StartActivity::class.java)
            intent.putExtra(Constants.FRAGMENT_POSITION, 2)
            startActivity(intent)
            finish()
            return
        }

        val adapter = chatAdapter
        recyclerView.layoutManager = LinearLayoutManager(this@ForumActivity)
        recyclerView.adapter = adapter
        recyclerView.smoothScrollToPosition(5)

        val lesson: Lesson? = intent.getParcelableExtra(FORUM_LESSON)
        val lessonTitle = lesson?.title
        val courseId = lesson?.courseId
        val lessonId = lesson?.id
        title = "Forum: $lessonTitle"

        val lessonRef = "forum_lesson_" + "$courseId" + "_$lessonId"
        val databaseReference = FirebaseDatabase.getInstance().getReference(lessonRef)
        databaseReference.orderByKey().addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val chatsList: ArrayList<Chat> = ArrayList()
                for (shot in dataSnapshot.children) {
                    val chat = shot.getValue<Chat>()
                    if (chat != null) {
                        chatsList.add(chat)
                    }
                }
                adapter.setData(chatsList)
                if (adapter.itemCount > 0) {
                    noChats.visibility = View.GONE
                    recyclerView.smoothScrollToPosition(adapter.itemCount - 1)
                } else {
                    noChats.visibility = View.VISIBLE
                }
                progressBar.visibility = View.GONE
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                val chat = dataSnapshot.getValue<Chat>()
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
                val chat = dataSnapshot.getValue<Chat>()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@ForumActivity, getString(R.string.failed_to_load_chats),
                        Toast.LENGTH_SHORT).show()
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }
        }
        databaseReference.orderByKey().addChildEventListener(childEventListener)

        sendBtn.setOnClickListener {
            val calendar: Calendar = Calendar.getInstance()
            val message = chatInput.text.toString()
            if (message.isEmpty()) {
                Toast.makeText(this@ForumActivity, getString(R.string.empty_chat_error), Toast.LENGTH_LONG).show()
            } else {
                val chat = Chat(username, message, calendar.timeInMillis)
                databaseReference.child(calendar.timeInMillis.toString()).setValue(chat)
                chatInput.setText("")
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}