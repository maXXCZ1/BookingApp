package opkp.solutions.bookingapp.calendar

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import opkp.solutions.bookingapp.R
import opkp.solutions.bookingapp.databinding.FragmentCalendarBinding
import opkp.solutions.bookingapp.viewmodels.SharedViewModel
import java.util.*
import kotlin.system.measureTimeMillis

/**
 * A simple [Fragment] subclass.
 * Use the [CalendarFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

private const val TAG = "CalendarFragment"

class CalendarFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var currentUser: FirebaseUser
    private lateinit var viewModel: SharedViewModel
    private lateinit var binding: FragmentCalendarBinding
    private var date: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        auth = FirebaseAuth.getInstance()
        currentUser = auth.currentUser!!


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate<FragmentCalendarBinding>(inflater,
            R.layout.fragment_calendar,
            container,
            false)

        viewModel.mapTimetoCourts = HashMap<String, List<Int>>()
        Log.d(TAG, "map time to courts is ${viewModel.mapTimetoCourts.isEmpty()}, bookingDatabase is ${viewModel.bookingListFromDB}")

        if(viewModel.bookingListFromDB.isEmpty()) {viewModel.loadBookingsFromDB()}


        binding.calendarview.setOnDateChangeListener { _, i, i2, i3 ->
            date = "$i3/${i2 + 1}/$i"
            viewModel.checkDate(date)
            Log.d(TAG, "date is $i3/${i2 + 1}/$i")
        }

        binding.buttonLogout.setOnClickListener {
            auth.signOut()
            findNavController().navigate(CalendarFragmentDirections.actionCalendarFragmentToLoginFragment())
        }

        viewModel.invalidDate.observe(viewLifecycleOwner, { isTrue ->
            binding.buttonNext2.isEnabled = !isTrue
        })

        binding.buttonNext2.setOnClickListener {
            Log.d(TAG, "Next button clicked")
            viewModel.pickDate(date)

            if(viewModel.bookingListFromDB.isNotEmpty()) {
                viewModel.checkBookedCourts()
                findNavController().navigate(CalendarFragmentDirections.actionCalendarFragmentToTimeFragment())
            } else { Toast.makeText(requireContext(), "Unable to load data from database, \n please try again later", Toast.LENGTH_SHORT).also {
               it.setGravity(Gravity.CENTER, 0,0)
               it.show()
            }
            }


        }

        Log.d(TAG,
            "onCreateView ended: Picked date is ${viewModel.pickedDate}, time is: ${viewModel.pickedTimeSlot}, court(s) are: ${viewModel.pickedCourt}")
        return binding.root
    }

}