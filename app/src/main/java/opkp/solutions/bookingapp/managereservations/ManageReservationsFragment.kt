package opkp.solutions.bookingapp.managereservations

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
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

        viewModel.savedPosition = -1

        viewModel.dataLoadCompleted.observe(viewLifecycleOwner) {
            if (!it) {
                binding.clProgressbar2.visibility = View.VISIBLE
                binding.buttonCancelReservation.isEnabled = false
                binding.btnBackToSummary.isClickable = false

            } else {
                viewModel.filteredList = mutableListOf()
                binding.clProgressbar2.visibility = View.GONE
                binding.btnBackToSummary.isClickable = true
                viewModel.filterBookingList()
                adapter = MyReservationItemAdapter(
                    viewModel.filteredList,
                    viewModel.currentUserID,
                    requireContext(),
                    -1,
                    false,
                    this,
                )
                binding.recyclerView.adapter = adapter
                binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())


            }
        }

        viewModel.loadBookingsFromDB()


        binding.buttonCancelReservation.setOnClickListener {

            getConfirmationFromDialog()
        }

        binding.btnBackToSummary.setOnClickListener {
            findNavController().navigate(ManageReservationsFragmentDirections.actionManageReservationsFragmentToSummaryFragment())
        }
        return binding.root
    }

    override fun onItemClick(position: Int) {


        if (!isItemClicked && viewModel.savedPosition == -1) {
            viewModel.savedPosition = position
            isItemClicked = true
            binding.buttonCancelReservation.isEnabled = true
            binding.buttonCancelReservation.setBackgroundColor(Color.RED)
        } else {
            if (isItemClicked && viewModel.savedPosition == position) {
                viewModel.savedPosition = -1
                isItemClicked = false
                binding.buttonCancelReservation.isEnabled = false
                binding.buttonCancelReservation.setBackgroundColor(Color.LTGRAY)
            }
        }


    }

    private fun getConfirmationFromDialog() {
        var confirmation: Boolean = false
        val alertDialog = AlertDialog.Builder(requireContext())

        with(alertDialog) {
            setTitle("Cancellation confirmation ")
            setMessage("Are you sure?")
            setIcon(R.drawable.ic_questionmark)
            setNegativeButton("Cancel") { _, _ ->
                deleteItemFromDB(confirmation)
            }
            setPositiveButton("OK") { _, _ ->
                confirmation = true
                deleteItemFromDB(confirmation)
            }
            show()
        }

    }

    private fun deleteItemFromDB(conf: Boolean) {

        if (conf) {
            viewModel.deleteBookingFromDB()
            viewModel.filteredList.removeAt(viewModel.savedPosition)
            adapter = MyReservationItemAdapter(
                viewModel.filteredList,
                viewModel.currentUserID,
                requireContext(),
                -1,
                false,
                this,
            )
            binding.recyclerView.adapter = adapter

            binding.buttonCancelReservation.isEnabled = false
            binding.buttonCancelReservation.setBackgroundColor(Color.LTGRAY)
            viewModel.savedPosition = -1
            isItemClicked = false
        } else {
            adapter = MyReservationItemAdapter(
                viewModel.filteredList,
                viewModel.currentUserID,
                requireContext(),
                -1,
                false,
                this,
            )
            binding.recyclerView.adapter = adapter
            binding.buttonCancelReservation.isEnabled = false
            binding.buttonCancelReservation.setBackgroundColor(Color.LTGRAY)
            viewModel.savedPosition = -1
            isItemClicked = false
        }
    }
}
