package opkp.solutions.bookingapp

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.activity_main.*

private const val TAG = "MainActivity"


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        setContentView(R.layout.activity_main)
    }


    override fun onBackPressed() {
        val currentFragment = navhost_fragment.findNavController().currentDestination?.id

        Log.d(TAG,
            "currentFragment is ${currentFragment}, summaryFragment id is: ${R.id.summaryFragment}, loginFragment is ${R.id.loginFragment}")

        if (currentFragment != null) {
            when (currentFragment) {
                R.id.calendarFragment -> {
                    Toast.makeText(applicationContext,
                        "Button disabled.\nUse buttons provided by application.",
                        Toast.LENGTH_SHORT)
                        .also {
                            it.setGravity(Gravity.CENTER, 0, 0)
                            it.show()
                        }
                }
                R.id.summaryFragment -> {
                    Toast.makeText(applicationContext,
                        "Button disabled.\nUse buttons provided by application.",
                        Toast.LENGTH_SHORT)
                        .also {
                            it.setGravity(Gravity.CENTER, 0, 0)
                            it.show()
                        }
                }
                R.id.loginFragment -> {
                    finishAffinity()
                }
                else -> super.onBackPressed()
            }

        }
    }
}





