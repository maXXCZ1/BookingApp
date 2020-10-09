package opkp.solutions.bookingapp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import opkp.solutions.bookingapp.R
import opkp.solutions.bookingapp.databinding.FragmentCalendarBinding

private const val  TAG = "CalendarFragment"
/**
 * A simple [Fragment] subclass.
 * Use the [CalendarFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CalendarFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentCalendarBinding>(inflater, R.layout.fragment_calendar, container, false)

        binding.button.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            findNavController().navigate(CalendarFragmentDirections.actionCalendarFragmentToLoginFragment())
        }
    Log.d(TAG, "testujeme" )
    return binding.root
    }

}