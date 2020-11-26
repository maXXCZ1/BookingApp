package opkp.solutions.bookingapp.managereservations

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import opkp.solutions.bookingapp.BookedData
import opkp.solutions.bookingapp.MyReservationItemAdapter
import opkp.solutions.bookingapp.R
import opkp.solutions.bookingapp.TimeItemAdapter
import opkp.solutions.bookingapp.databinding.FragmentManageReservationsBinding
import opkp.solutions.bookingapp.viewmodels.SharedViewModel

private const val TAG = "MNGReservationsFragment"

/**
 * A simple [Fragment] subclass.
 * Use the [ManageReservationsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ManageReservationsFragment : Fragment() {


    private lateinit var binding: FragmentManageReservationsBinding
    private lateinit var adapter: MyReservationItemAdapter

    private lateinit var viewModel: SharedViewModel

    //TODO show all my reservations in RecyclerView and let user to cancel chosen one

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_manage_reservations,
            container,
            false)

        viewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        viewModel.dataLoadCompleted.observe(viewLifecycleOwner) {
            if (!it) {
                binding.clProgressbar2.visibility = View.VISIBLE
                binding.buttonCancelReservation.isClickable = false
                binding.btnBackToSummary.isClickable = false

            } else {
                binding.clProgressbar2.visibility = View.GONE
                binding.buttonCancelReservation.isClickable = true
                binding.btnBackToSummary.isClickable = true
                Log.d(TAG, "bookingListFromDB size is ${viewModel.bookingListFromDB.size} ")

                val filteredList: MutableList<BookedData> = mutableListOf<BookedData>()
                for (booking in viewModel.bookingListFromDB) {
                    if (booking.currentUserID == viewModel.currentUserID) {
                        filteredList.add(booking)

                    }
                }
                adapter = MyReservationItemAdapter(filteredList, viewModel.currentUserID)

                binding.recyclerView.adapter = adapter
                binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
            }
        }

        viewModel.loadBookingsFromDB()




        binding.buttonCancelReservation.setOnClickListener {
            //TODO implement deleting picked entry from firebase
            Log.d(TAG, "cancel reservation clicked")
        }

        binding.btnBackToSummary.setOnClickListener {
            findNavController().navigate(ManageReservationsFragmentDirections.actionManageReservationsFragmentToSummaryFragment())
        }
        return binding.root
    }

}