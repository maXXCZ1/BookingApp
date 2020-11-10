package opkp.solutions.bookingapp.summary

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
import opkp.solutions.bookingapp.R
import opkp.solutions.bookingapp.databinding.FragmentSummaryBinding
import opkp.solutions.bookingapp.viewmodels.SharedViewModel

private const val TAG = "SummaryFragment"

/**
 * A simple [Fragment] subclass.
 * Use the [SummaryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SummaryFragment : Fragment() {

    private lateinit var viewModel: SharedViewModel
    private lateinit var binding: FragmentSummaryBinding
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()

        viewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_summary, container, false)


        val dateText = "Day: ${viewModel.pickedDate}"
        val timeText = "Time: ${viewModel.pickedTimeSlot}"
        val courtText = "Court(s): ${viewModel.pickedCourt.sorted()}"

        binding.tvSummaryday.text = dateText

        binding.tvSummarytimeslot.text = timeText

        Log.d(TAG, "pickedCourt(s) are: ${viewModel.pickedCourt.sorted()}")


        binding.tvSummarycourtnumber.text = courtText
            .replace("[", "")
            .replace("]", "")


        binding.btnNewReservation.setOnClickListener {
            findNavController().navigate(SummaryFragmentDirections.actionSummaryFragmentToCalendarFragment())
            viewModel.initializeSharedViewModel()
        }
        binding.btnSummaryLogout.setOnClickListener {
            auth.signOut()
            viewModel.initializeSharedViewModel()
            findNavController().navigate(SummaryFragmentDirections.actionSummaryFragmentToLoginFragment())
        }

        binding.btnManageReservations.setOnClickListener {
            findNavController().navigate(SummaryFragmentDirections.actionSummaryFragmentToManageReservationsFragment())
        }

        return binding.root
    }


}