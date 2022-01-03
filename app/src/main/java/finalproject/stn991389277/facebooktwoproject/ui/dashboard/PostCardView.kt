package finalproject.stn991389277.facebooktwoproject.ui.dashboard
//OWNER: LUC LAFFIN 991389277 & JOSHUA CLARK 991516472
//THIS ACTIVITY IS GENERATES DATA FOR THE RECYCLER VIEW
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import finalproject.stn991389277.facebooktwoproject.R
import finalproject.stn991389277.facebooktwoproject.databinding.FragmentDashboardBinding

class PostCardView (private val friendsPosts: List <PostListItem>): RecyclerView.Adapter <PostCardView.MyViewHolder>() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val profileImage : ImageView = itemView.findViewById(R.id.friendImageView)
        val friendName : TextView = itemView.findViewById(R.id.friendNameBox)
        val friendPost : TextView = itemView.findViewById(R.id.postView)
        val timestamp : TextView = itemView.findViewById(R.id.timeStamp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.postcardview,
            parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = friendsPosts [position]
        holder.profileImage.setImageResource(currentItem.imageResource)
        holder.friendName.text = currentItem.friendName
        holder.friendPost.text = currentItem.post
        holder.timestamp.text = currentItem.timestmap
    }

    override fun getItemCount() = friendsPosts.size
}