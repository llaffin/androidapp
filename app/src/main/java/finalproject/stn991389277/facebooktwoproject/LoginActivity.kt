package finalproject.stn991389277.facebooktwoproject

//OWNER: JOSHUA CLARK 991516472
//THIS ACTIVITY HANDLES USER LOGIN TO THE APP

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import finalproject.stn991389277.facebooktwoproject.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    //initialize firebase
    private lateinit var binding: ActivityLoginBinding
    private var fAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Redirect to MainActivity if they are logged in
        if (fAuth.currentUser != null) {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        }

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        //LOGIN BUTTON
        binding.loginBtn.setOnClickListener {
            loginBtnOnClick()
        }

        //GO TO REGISTRATION
        binding.goToRegistration.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegistrationActivity::class.java))
        }
    }

    private fun loginBtnOnClick() {
        val email = binding.usrnameLoginInput.text
        val password = binding.pwdLoginInput.text

        //check validation
        if (TextUtils.isEmpty(email)) {
            binding.usrnameLoginInput.error = "Email is required."
            return
        }
        if (TextUtils.isEmpty(password)) {
            binding.pwdLoginInput.error = "Password is required."
            return
        }

        //show progress bar
        binding.progressBar2.isVisible = true

        //login user
        fAuth.signInWithEmailAndPassword(email.toString(), password.toString())
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Logged In Successfully", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                } else {
                    Toast.makeText(
                        this,
                        "Invalid Login Credentials",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.progressBar2.isVisible = false
                }
            }
    }
}