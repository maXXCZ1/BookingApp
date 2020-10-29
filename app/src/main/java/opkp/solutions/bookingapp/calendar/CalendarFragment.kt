package opkp.solutions.bookingapp.calendar

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import opkp.solutions.bookingapp.R
import opkp.solutions.bookingapp.databinding.FragmentCalendarBinding
import opkp.solutions.bookingapp.viewmodels.SharedViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [CalendarFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

private const val  TAG = "CalendarFragment"

class CalendarFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var currentUser: FirebaseUser
    private lateinit var viewModel: SharedViewModel
    private lateinit var binding: FragmentCalendarBinding
    private var date: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
        currentUser = auth.currentUser!!

        viewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate<FragmentCalendarBinding>(inflater, R.layout.fragment_calendar, container, false)

        binding.calendarview.setOnDateChangeListener { _, i, i2, i3 ->
            date = "$i3/${i2+1}/$i"
            viewModel.checkDate(date)
            Log.d(TAG, "date is $i3/${i2+1}/$i")
        }

        binding.buttonLogout.setOnClickListener{

            FirebaseAuth.getInstance().signOut()
            findNavController().navigate(CalendarFragmentDirections.actionCalendarFragmentToLoginFragment())
        }

        viewModel.invalidDate.observe(viewLifecycleOwner, { isTrue ->
            binding.buttonNext2.isEnabled = !isTrue
        })

        binding.buttonNext2.setOnClickListener{
            viewModel.pickDate(date)
            findNavController().navigate(CalendarFragmentDirections.actionCalendarFragmentToTimeFragment())
        }

        Log.d(TAG, "onCreateView ended: Picked date is ${viewModel.pickedDate}")
        return binding.root
    }


}