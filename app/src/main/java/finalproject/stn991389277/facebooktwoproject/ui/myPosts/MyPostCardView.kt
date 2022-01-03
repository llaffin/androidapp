package finalproject.stn991389277.facebooktwoproject.ui.myPosts

//OWNER: LUC LAFFIN 991389277 & JOSHUA CLARK 991516472
//THIS ACTIVITY IS GENERATES DATA FOR THE RECYCLER VIEW

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import finalproject.stn991389277.facebooktwoproject.MainActivity
import finalproject.stn991389277.facebooktwoproject.R
import finalproject.stn991389277.facebooktwoproject.databinding.FragmentMypostsBinding
import finalproject.stn991389277.facebooktwoproject.ui.dashboard.MyPostListItem
import kotlin.coroutines.coroutineContext

class MyPostCardView (private val myPosts: List <MyPostListItem>): RecyclerView.Adapter <MyPostCardView.MyViewHolder>() {

    private var _binding: FragmentMypostsBinding? = null
    private var fStore = FirebaseFirestore.getInstance()

    private val binding get() = _binding!!

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val profileImage : ImageView = itemView.findViewById(R.id.myImageView)
        val myName : TextView = itemView.findViewById(R.id.myTimeStamp)
        val myPost : TextView = itemView.findViewById(R.id.postBox)
        val postID : TextView = itemView.findViewById(R.id.postID)
        val deleteBtn : Button = itemView.findViewById(R.id.deletePostBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.mypostscardview,
            parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = myPosts [position]
        holder.profileImage.setImageResource(currentItem.imageResource)
        holder.myName.text = currentItem.timestamp
        holder.myPost.text = currentItem.post
        holder.postID.text = currentItem.postID
        holder.postID.isVisible = false
        holder.deleteBtn.setOnClickListener{
            var postID: String = holder.postID.text.toString()
            var docReference = fStore.collection("posts").document(postID)
            docReference.delete()
        }
    }

    override fun getItemCount() = myPosts.size
}