package finalproject.stn991389277.facebooktwoproject

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import finalproject.stn991389277.facebooktwoproject.databinding.ActivityMainBinding
import java.lang.Exception
import finalproject.stn991389277.facebooktwoproject.ui.profile.ProfileFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var fAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Redirect user to login page if they are not logged in
        if (fAuth.currentUser == null) {
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
        }

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_profile, R.id.navigation_dashboard, R.id.navigation_posts
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.helpmenu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_help -> {
            val dialogBuilder = AlertDialog.Builder(this)
            val alert = dialogBuilder.create()

            alert.setTitle("Help Menu")
            alert.setMessage("This is FacebookTwo.\nOnce logged in, the default page is your " +
                    "profile where you can edit your bio, add & remove friends, and logout.\n" +
            "Dashboard: here is where you can view all of your friends posts and see what they're up to!\n" +
            "My Posts: here is where you can add your own posts, view them, and delete them.")
            alert.setIcon(R.drawable.common_google_signin_btn_icon_dark)
            alert.show()
            true
        }
        R.id.action_about -> {
            val dialogBuilder = AlertDialog.Builder(this)
            val alert = dialogBuilder.create()

            alert.setTitle("About")
            alert.setMessage("This is FacebookTwo.\n" +
            "Submission Date: December 8th 2021\n" +
            "Developers: \n\tJoshua Silva Clark 991516472\n\tLuc Laffin 991389277\n")
            alert.setIcon(R.drawable.common_google_signin_btn_icon_dark)
            alert.show()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }


}
