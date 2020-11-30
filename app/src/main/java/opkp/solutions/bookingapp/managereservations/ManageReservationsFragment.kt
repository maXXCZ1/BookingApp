package opkp.solutions.bookingapp.managereservations

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import opkp.solutions.bookingapp.MyReservationItemAdapter
import opkp.solutions.bookingapp.R
import opkp.solutions.bookingapp.databinding.FragmentManageReservationsBinding
import opkp.solutions.bookingapp.viewmodels.SharedViewModel

private const val TAG = "MNGReservationsFragment"

/**
 * A simple [Fragment] subclass.
 * Use the [ManageReservationsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ManageReservationsFragment : Fragment(),
    MyReservationItemAdapter.OnMyReservationItemClickListener {


    private lateinit var binding: FragmentManageReservationsBinding
    private lateinit var adapter: MyReservationItemAdapter
    private lateinit var viewModel: SharedViewModel



    private var isItemClicked = false
    private var savedPosition: Int = -1

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
                binding.btnBackToSummary.isClickable = true
                Log.d(TAG, "bookingListFromDB size is ${viewModel.bookingListFromDB.size} ")

                viewModel.filterBookingList()
                adapter =
                    MyReservationItemAdapter(viewModel.filteredList, viewModel.currentUserID, this)
                binding.recyclerView.adapter = adapter
                binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())


            }
        }

        viewModel.loadBookingsFromDB()


        binding.buttonCancelReservation.setOnClickListener {
            if(savedPosition != -1) {
                viewModel.filteredList.removeAt(savedPosition)
                adapter.notifyDataSetChanged()
                binding.buttonCancelReservation.isEnabled = false
                savedPosition = -1
                isItemClicked = false
            } else {
                Toast.makeText(requireContext(),
                    "Something went wrong while deleting Reservation",
                    Toast.LENGTH_SHORT).show()
            }
            Log.d(TAG, "cancel reservation clicked")
        }

        binding.btnBackToSummary.setOnClickListener {
            findNavController().navigate(ManageReservationsFragmentDirections.actionManageReservationsFragmentToSummaryFragment())
        }
        return binding.root
    }

    //TODO How do i change background color on single recyclerView item?!?!?!
    override fun onItemClick(position: Int) {
        Log.d(TAG, "onItemClick started, recyclerview childcount ${binding.recyclerView.childCount}")

        if (!isItemClicked && savedPosition == -1) {
//            binding.recyclerView[position].setBackgroundResource(R.drawable.customborder_blue)
            savedPosition = position
            isItemClicked = true
            binding.buttonCancelReservation.isEnabled = true
        } else {
            if (isItemClicked && savedPosition == position) {
//                binding.recyclerView[position].setBackgroundResource(R.drawable.customborder)
                savedPosition = -1
                isItemClicked = false
                binding.buttonCancelReservation.isEnabled = false

            }
            else {
                Toast.makeText(requireContext(),
                    "You can choose only one item, currently chosen item is no: $savedPosition",
                    Toast.LENGTH_SHORT).show()
            }
        }

        Log.d(TAG, "isItemClicked is $isItemClicked, savedPosition is $savedPosition, position is $position")
    }
}