package finalproject.stn991389277.facebooktwoproject.ui.profile

//OWNER: JOSHUA CLARK 991516472
//THIS ACTIVITY IS THE USER BIO WHERE THEY CAN ADD/DELETE FRIENDS, VIEW FRIENDS

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import finalproject.stn991389277.facebooktwoproject.model.Name

class ProfileViewModel : ViewModel() {

    private var fAuth = FirebaseAuth.getInstance()
    private var fStore = FirebaseFirestore.getInstance()
    private var userID = fAuth.currentUser!!.uid


    private val nameModel: Name = Name(userID)


    private val _name: MutableLiveData<String> = MutableLiveData<String>()
    val name: LiveData<String>
        get() = _name

    private val _email = MutableLiveData<String>().apply {
        var userID = fAuth.currentUser!!.uid

        if (userID != null) {
            var docReference = fStore.collection("users").document(userID)

            docReference.get().addOnSuccessListener { doc ->
                value = doc.getString("email")
            }
        }
    }
    val email: LiveData<String>
        get() = _name


    private val _bio = MutableLiveData<String>().apply {
        value = "Hi, I'm $_name!"
    }
    val bio: LiveData<String>
        get() = _bio

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String>
        get() = _text

    init {


        var docReference = fStore.collection("users").document(userID)

        docReference.get().addOnSuccessListener { doc ->
            var n = doc.getString("name").toString()
            nameModel.value = n
        }

        _name.value = nameModel.value

    }

}