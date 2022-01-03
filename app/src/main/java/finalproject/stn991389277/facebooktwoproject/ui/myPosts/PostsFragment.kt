package finalproject.stn991389277.facebooktwoproject.ui.myPosts

//OWNER: LUC LAFFIN 991389277
//THIS ACTIVITY IS WHERE THE USER CAN ADD POSTS, VIEW THEIR POSTS, AND DELETE THEIR POSTS

import android.content.ContentValues
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import finalproject.stn991389277.facebooktwoproject.R
import finalproject.stn991389277.facebooktwoproject.databinding.FragmentMypostsBinding
import finalproject.stn991389277.facebooktwoproject.databinding.FragmentProfileBinding
import finalproject.stn991389277.facebooktwoproject.databinding.MypostscardviewBinding
import finalproject.stn991389277.facebooktwoproject.ui.dashboard.MyPostListItem
import kotlinx.coroutines.Dispatchers
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class PostsFragment : Fragment() {

    private lateinit var postsViewModel: PostsViewModel
    private lateinit var binding: FragmentMypostsBinding
    private var fAuth = FirebaseAuth.getInstance()
    private var fStore = FirebaseFirestore.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        postsViewModel =
            ViewModelProvider(this).get(PostsViewModel::class.java)

        binding = FragmentMypostsBinding.inflate(inflater, container, false)

        //generate card views
        getPostData()

        //ADD POST BUTTON
        binding.addpostBtn.setOnClickListener {
            var post: String = binding.newpostBox.text.toString()
            if (TextUtils.isEmpty(post)) {
                binding.newpostBox.error = "Post cannot be empty."
                return@setOnClickListener
            }
            val userID = fAuth.currentUser!!.uid
            val docReference = fStore.collection("posts")
            var newPost: MutableMap<String, Any> = HashMap()
            newPost["post"] = post
            newPost["poster"] = userID
            val sdf = SimpleDateFormat("yyyy/MM/dd hh:mm:ss")
            val currentDate = sdf.format(Date())
            newPost["time"] = currentDate.toString()
            docReference.add(newPost)
                .addOnSuccessListener {
                    Log.d(ContentValues.TAG, "onSuccess: post added for $userID")
                    Toast.makeText(activity, "Post Added!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Log.w(ContentValues.TAG, "onFailure: error adding post for $userID")
                    Toast.makeText(activity, "Error: Post Not Added", Toast.LENGTH_SHORT).show()
                }

            getPostData()
        }

        return binding.root
    }

    private fun generateMyPosts(myPosts: List<MyPostListItem>) {
        binding.recyclerView.adapter = MyPostCardView(myPosts)
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.setHasFixedSize(true)
    }

    private fun getPostData() {

        var cards = ArrayList<MyPostListItem>()
        var userID = fAuth.currentUser!!.uid
        var docReference = fStore.collection("posts").whereEqualTo("poster", userID)
        docReference.get().addOnSuccessListener { docs ->
            for (doc in docs) {
                var post = doc.getString("post").toString()
                var timestamp = doc.getString("time").toString()
                var id = doc.id
                var image = R.drawable.ic_launcher_foreground
                val item = MyPostListItem(image, timestamp, post, id)
                cards += item
            }
            generateMyPosts(cards)
        }
    }
}