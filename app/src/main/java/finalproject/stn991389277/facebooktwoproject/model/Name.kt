package finalproject.stn991389277.facebooktwoproject.model

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Name (userID: String) {

    private var fStore = FirebaseFirestore.getInstance()

    var value: String = ""

    init {
        if (userID != null) {
            var docReference = fStore.collection("users").document(userID)

            docReference.get().addOnSuccessListener { doc ->
                value = doc.getString("name").toString()
            }
        }
    }
}