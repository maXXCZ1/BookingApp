package opkp.solutions.bookingapp

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import kotlinx.android.synthetic.main.activity_main.*
import opkp.solutions.bookingapp.summary.SummaryFragment

private const val TAG = "MainActivity"

    private lateinit var currentFragment: NavDestination
    private lateinit var summaryFragment: NavHostFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }



    override fun onBackPressed() {
        val currentFragment = navhost_fragment.findNavController().currentDestination?.id

        Log.d(TAG, "currentFragment is ${currentFragment}, summaryFragment id is: ${R.id.summaryFragment}, loginFragment is ${R.id.loginFragment}")


        if(currentFragment != null && currentFragment == R.id.calendarFragment) {
            Toast.makeText(applicationContext,
                "Button disabled.\nUse buttons provided by application.",
                Toast.LENGTH_SHORT)
                .also {
                    it.setGravity(Gravity.CENTER, 0, 0)
                    it.show()
                }
        }

        if(currentFragment != null && currentFragment == R.id.summaryFragment) {

            Toast.makeText(applicationContext,
                "Button disabled.\nUse buttons provided by application.",
                Toast.LENGTH_SHORT)
                .also {
                    it.setGravity(Gravity.CENTER, 0, 0)
                    it.show()
                }

        }
        else super.onBackPressed()

    }

}
