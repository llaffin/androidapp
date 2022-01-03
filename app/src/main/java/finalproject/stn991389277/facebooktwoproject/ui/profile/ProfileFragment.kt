package finalproject.stn991389277.facebooktwoproject.ui.profile
//OWNER: JOSHUA CLARK 991516472
//THIS ACTIVITY IS THE USER BIO WHERE THEY CAN ADD/DELETE FRIENDS, VIEW FRIENDS

import android.content.ContentValues
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.InputType
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import finalproject.stn991389277.facebooktwoproject.LoginActivity
import finalproject.stn991389277.facebooktwoproject.databinding.FragmentProfileBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private var fAuth = FirebaseAuth.getInstance()
    private var fStore = FirebaseFirestore.getInstance()
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var binding: FragmentProfileBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        profileViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)

        binding = FragmentProfileBinding.inflate(inflater, container, false)

        //EDIT BIO BUTTON
        binding.bioBtn.setOnClickListener {
            binding.userBio.inputType = InputType.TYPE_CLASS_TEXT
            binding.userBio.isCursorVisible = true
            binding.userBio.isFocusableInTouchMode = true
            binding.userBio.canScrollHorizontally(0)
            binding.userBio.requestFocus()

            binding.bioBtn.isVisible = false
            binding.saveBtn.isVisible = true
            binding.cancelBtn.isVisible = true
        }

        //SAVE BIO UPDATE
        binding.saveBtn.setOnClickListener {

            //save the new bio
            var newBio: String = binding.userBio.text.toString()
            val userID = fAuth.currentUser!!.uid
            val docReference = fStore.collection("users").document(userID)
            docReference.update("bio", newBio)
                .addOnSuccessListener {
                    Log.d(ContentValues.TAG, "onSuccess: user bio updated for $userID")
                }
                .addOnFailureListener {
                    Log.w(ContentValues.TAG, "onFailure: error updating user bio for $userID")
                }

            //change button visibility
            binding.userBio.inputType = InputType.TYPE_NULL
            binding.userBio.isCursorVisible = false
            binding.userBio.isFocusableInTouchMode = false
            binding.userBio.clearFocus()

            binding.bioBtn.isVisible = true
            binding.cancelBtn.isVisible = false
            binding.saveBtn.isVisible = false
        }

        //CANCEL BIO CHANGE BUTTON
        binding.cancelBtn.setOnClickListener {
            binding.userBio.inputType = InputType.TYPE_NULL
            binding.userBio.isCursorVisible = false
            binding.userBio.isFocusableInTouchMode = false
            binding.userBio.clearFocus()

            binding.bioBtn.isVisible = true
            binding.cancelBtn.isVisible = false
            binding.saveBtn.isVisible = false
        }

        //ADD FRIEND BUTTON
        binding.addFriendBtn.setOnClickListener {
            var friendName: String = binding.addFriendTv.text.toString().trim()
            if (TextUtils.isEmpty(friendName)) {
                binding.addFriendTv.error = "Friend Name cannot be empty."
                return@setOnClickListener
            }
            var friendID = ""

            //find the Friend's UserID
            fStore.collection("users").whereEqualTo("name", friendName)
                .limit(1)
                .get()
                .addOnSuccessListener { docs ->
                    for (doc in docs) {
                        friendID = doc.id
                        if (!TextUtils.isEmpty(friendID)) {

                            val userID = fAuth.currentUser!!.uid
                            val docReference = fStore.collection("users").document(userID)

                            //get list of friends
                            docReference.get()
                                .addOnSuccessListener {
                                    if (docReference != null) {
                                        var friends = it.get("friends") as MutableList<String>
                                        //add new friend to list
                                        friends.add(friendID)
                                        //save new friendList
                                        docReference.update("friends", friends)
                                            .addOnSuccessListener {
                                                Log.d(
                                                    ContentValues.TAG,
                                                    "onSuccess: user friend list updated for $userID"
                                                )
                                            }
                                            .addOnFailureListener {
                                                Log.w(
                                                    ContentValues.TAG,
                                                    "onFailure: error updating friend list for $userID"
                                                )
                                            }
                                    }
                                }
                                .addOnFailureListener {
                                    Log.w(
                                        ContentValues.TAG,
                                        "onFailure: error updating friend list for $userID"
                                    )
                                }
                        }
                    }
                }
                .addOnFailureListener {
                    friendID = "fail"
                }
        }

        //DELETE FRIEND BUTTON
        binding.removeFriendBtn.setOnClickListener {
            var friendName: String = binding.addFriendTv.text.toString().trim()
            if (TextUtils.isEmpty(friendName)) {
                binding.addFriendTv.error = "Friend Name cannot be empty."
                return@setOnClickListener
            }
            var friendID = ""

            //find the Friend's UserID
            fStore.collection("users").whereEqualTo("name", friendName)
                .limit(1)
                .get()
                .addOnSuccessListener { docs ->
                    for (doc in docs) {
                        friendID = doc.id
                        if (!TextUtils.isEmpty(friendID)) {

                            val userID = fAuth.currentUser!!.uid
                            val docReference = fStore.collection("users").document(userID)

                            //get list of friends
                            docReference.get()
                                .addOnSuccessListener {
                                    if (docReference != null) {
                                        var friends = it.get("friends") as MutableList<String>
                                        //add new friend to list
                                        friends.remove(friendID)
                                        //save new friendList
                                        docReference.update("friends", friends)
                                            .addOnSuccessListener {
                                                Log.d(
                                                    ContentValues.TAG,
                                                    "onSuccess: user friend list updated for $userID"
                                                )
                                            }
                                            .addOnFailureListener {
                                                Log.w(
                                                    ContentValues.TAG,
                                                    "onFailure: error updating friend list for $userID"
                                                )
                                            }
                                    }
                                }
                                .addOnFailureListener {
                                    Log.w(
                                        ContentValues.TAG,
                                        "onFailure: error updating friend list for $userID"
                                    )
                                }
                        }
                    }
                }
                .addOnFailureListener {
                    friendID = "fail"
                }
        }

        //INITIALIZE UI

        return binding.root
    }

    private suspend fun getName() {
        delay(2000L)
        binding.bioName.text = profileViewModel.name.value

    }

    private fun displayName(name: String) {
        binding.bioName.text = name
    }

    @Override
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var userID = fAuth.currentUser!!.uid
        var docReference = fStore.collection("users").document(userID)
        activity?.let {
            docReference.addSnapshotListener(
                it,
                EventListener() { documentSnapshot: DocumentSnapshot?, _: FirebaseFirestoreException? ->
                    binding.bioName.text = documentSnapshot!!.getString("name")
                    binding.bioEmail.text = documentSnapshot!!.getString("email")
                    binding.userBio.text = documentSnapshot!!.getString("bio")


                    var friendListAsId = documentSnapshot!!.get("friends") as List<String>
                    var friendListAsName: MutableList<String> = arrayListOf()
                    for (friend in friendListAsId) {
                        if(!TextUtils.isEmpty(friend)) {
                            fStore.collection("users").document(friend)
                                .get()
                                .addOnSuccessListener { doc ->
                                    friendListAsName.add(doc.getString("name").toString())
                                    binding.friendsTv.text = friendListAsName.toString()
                                }
                        }
                    }
                })

            //LOGOUT BUTTON
            binding.logoutBtn.setOnClickListener {
                fAuth.signOut()
                Toast.makeText(activity, "Logged Out", Toast.LENGTH_SHORT).show()
                startActivity(Intent(activity, LoginActivity::class.java))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}


