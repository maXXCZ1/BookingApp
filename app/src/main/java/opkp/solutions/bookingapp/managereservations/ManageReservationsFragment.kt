package opkp.solutions.bookingapp.managereservations

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
class ManageReservationsFragment : Fragment(), MyReservationItemAdapter.OnMyReservationItemClickListener {


    private lateinit var binding: FragmentManageReservationsBinding
    private lateinit var adapter: MyReservationItemAdapter
    private lateinit var viewModel: SharedViewModel

    private var isItemClicked = 0
    var savedPosition: Int = -1

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
                binding.buttonCancelReservation.isEnabled = false
                binding.btnBackToSummary.isClickable = false

            } else {
                viewModel.filteredList = mutableListOf()
                binding.clProgressbar2.visibility = View.GONE
//                binding.buttonCancelReservation.isClickable = true
                binding.btnBackToSummary.isClickable = true
                Log.d(TAG, "bookingListFromDB size is ${viewModel.bookingListFromDB.size} ")

                viewModel.filterBookingList()

                adapter = MyReservationItemAdapter(viewModel.filteredList, viewModel.currentUserID, this)
                binding.recyclerView.adapter = adapter
                binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                binding.recyclerView.setHasFixedSize(true)
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

    override fun onItemClick(currentView: View, position: Int) {

        Log.d(TAG, "onItemclick started: position is $position, savedPosition is $savedPosition")
        val currentItem = viewModel.filteredList[position]
        val myView = currentView



        if(savedPosition != position && isItemClicked == 0) {
            myView.setBackgroundResource(R.drawable.customborder_blue)
            binding.buttonCancelReservation.isEnabled = true
            isItemClicked = 1
            savedPosition = position
            Log.d(TAG, "savedPosition is $savedPosition, position is $position test date is ${currentItem.dateFromDB}, isItemClicked $isItemClicked,\ncurrentItem courts are ${viewModel.filteredList[position].courtsFromDB}")

        } else {
            if (position == savedPosition && isItemClicked == 1) {
                myView.setBackgroundResource(R.drawable.customborder)
                binding.buttonCancelReservation.isEnabled = false
                isItemClicked = 0
                savedPosition = -1
                Log.d(TAG,"test date is ${currentItem.dateFromDB}, itemclick is $isItemClicked, position is $position, savedPosition is $savedPosition")

            }
            else {
                Log.d(TAG, "You can choose only one reservation, test date is ${currentItem.dateFromDB}, itemclick is $isItemClicked, position is $position, savedPosition is $savedPosition")
                Toast.makeText(requireContext(), "You can choose only one reservation", Toast.LENGTH_SHORT).also {
                    it.setGravity(Gravity.CENTER, 0,0)
                    it.show()
                }
            }
        }
//        Log.d(TAG, "")
//        adapter.notifyItemChanged(position)
    }

}