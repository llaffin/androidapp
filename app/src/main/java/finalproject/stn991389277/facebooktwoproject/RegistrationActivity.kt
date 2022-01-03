package finalproject.stn991389277.facebooktwoproject
//OWNER: LUC LAFFIN 99138927
//THIS ACTIVITY HANDLES USER REGISTRATION TO THE APP

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import finalproject.stn991389277.facebooktwoproject.databinding.ActivityRegistrationBinding

class RegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding
    private var fAuth = FirebaseAuth.getInstance()
    private var fStore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_registration)

        if (fAuth.currentUser != null) {
            startActivity(Intent(this@RegistrationActivity, MainActivity::class.java))
        }

        //REGISTRATION BUTTON
        binding.registrationBtn.setOnClickListener {
            registrationBtnOnClick()
        }

        //GO TO LOGIN
        binding.goToLogin.setOnClickListener {
            startActivity(Intent(this@RegistrationActivity, LoginActivity::class.java))
        }
    }

    private fun registrationBtnOnClick() {
        val fullName = binding.nameRegistrationInput.text.toString()
        val email = binding.usrnameRegistrationInput.text.toString()
        val password = binding.pwdRegistrationInput.text.toString()

        //check validation
        if (TextUtils.isEmpty(email)) {
            binding.usrnameRegistrationInput.error = "Email is required."
            return
        }
        if (TextUtils.isEmpty(password)) {
            binding.pwdRegistrationInput.error = "Password is required."
            return
        }
        if (password.length < 6) {
            binding.pwdRegistrationInput.error = "Password must be at least 6 characters."
        }

        //show progress bar
        binding.progressBar.isVisible = true

        //create user
        fAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this, "User Created", Toast.LENGTH_SHORT).show()

                    //save user data
                    val userID = fAuth.currentUser!!.uid
                    val docReference = fStore.collection("users").document(userID)
                    val user: MutableMap<String, Any> = HashMap()
                    user["name"] = fullName
                    user["email"] = email
                    user["bio"] = "Hi, I'm $fullName!"
                    user["friends"] = arrayListOf("")
                    docReference.set(user)

                    //redirect to main activity
                    startActivity(Intent(this@RegistrationActivity, MainActivity::class.java))
                } else {
                    Toast.makeText(
                        this,
                        "Error ! " + it.exception!!.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.progressBar.isVisible = false
                }
            }
    }
}