package finalproject.stn991389277.facebooktwoproject.ui.dashboard
//OWNER: LUC LAFFIN 991389277
//THIS ACTIVITY IS WHERE THE USER CAN ADD POSTS, VIEW THEIR POSTS, AND DELETE THEIR POSTS
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import finalproject.stn991389277.facebooktwoproject.R
import finalproject.stn991389277.facebooktwoproject.databinding.FragmentDashboardBinding
import finalproject.stn991389277.facebooktwoproject.databinding.ActivityLoginBinding
import finalproject.stn991389277.facebooktwoproject.databinding.FragmentMypostsBinding
import finalproject.stn991389277.facebooktwoproject.ui.myPosts.MyPostCardView

class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var binding: FragmentDashboardBinding
    private var fAuth = FirebaseAuth.getInstance()
    private var fStore = FirebaseFirestore.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        binding = FragmentDashboardBinding.inflate(inflater, container, false)

        getPostData()

        return binding.root
    }

    private fun generateMyPosts(myPosts: List<PostListItem>) {
        binding.recyclerView.adapter = PostCardView(myPosts)
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.setHasFixedSize(true)
    }

    private fun getPostData() {

        var cards = ArrayList<PostListItem>()
        var userID = fAuth.currentUser!!.uid

        //get friends list
        fStore.collection("users").document(userID)
            .get().addOnSuccessListener { docs ->
                var friends = docs.get("friends") as List<String>

                var docReference = fStore.collection("posts")

                docReference.get().addOnSuccessListener { docs ->
                    for (doc in docs) {
                        var friend = doc.getString("poster").toString()
                        if (friends.contains(friend)) {
                                var post = doc.getString("post").toString()
                                var timestamp = doc.getString("time").toString()
                                var friendName = doc.id
                                var image = R.drawable.ic_launcher_foreground
                                val item = PostListItem(image, timestamp, post, friendName)
                                cards += item
                            }
                    }
                    generateMyPosts(cards)
                }
            }
    }
}