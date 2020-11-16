package opkp.solutions.bookingapp.time


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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import opkp.solutions.bookingapp.R
import opkp.solutions.bookingapp.TimeItemAdapter
import opkp.solutions.bookingapp.databinding.FragmentTimeBinding
import opkp.solutions.bookingapp.viewmodels.SharedViewModel


/**
 * A simple [Fragment] subclass.
 * Use the [TimeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

private const val TAG = "TimeFragment"

class TimeFragment : Fragment(), TimeItemAdapter.OnItemClickListener {

    private lateinit var viewModel: SharedViewModel
    private lateinit var adapter: TimeItemAdapter
    private lateinit var binding: FragmentTimeBinding
    private var itemClick = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate<FragmentTimeBinding>(
            inflater,
            R.layout.fragment_time,
            container,
            false
        )

        adapter = TimeItemAdapter(viewModel.itemList, this, requireContext())

        binding.timeitemRecyclerview.adapter = adapter
        if(viewModel.linearLayout) {
            binding.timeitemRecyclerview.layoutManager = LinearLayoutManager(requireActivity())

        } else {
            binding.timeitemRecyclerview.layoutManager = GridLayoutManager(requireActivity(), 2)
        }
        binding.timeitemRecyclerview.setHasFixedSize(true)

        binding.buttonPrevious.setOnClickListener {
            findNavController().navigate(TimeFragmentDirections.actionTimeFragmentToCalendarFragment())
        }

        binding.buttonNext.isEnabled = itemClick != 0

        binding.buttonNext.setOnClickListener {

            Log.d(TAG,"next button clicked: Picked date is ${viewModel.pickedDate}, picked timeslot is ${viewModel.pickedTimeSlot}")
            findNavController().navigate(TimeFragmentDirections.actionTimeFragmentToCourtFragment())
            }

        return binding.root
    }

    override fun onItemClick(position: Int) {

        val item = viewModel.itemList[position]
        Log.d(TAG, "pickedTimeSlot is ${viewModel.pickedTimeSlot}, position is $position, first item status is ${viewModel.itemList[0].status}")

                if (item.status == "Unavailable") {
                    item.layoutBG = R.drawable.customborder
                    Toast.makeText(requireContext(), "Choose another date please.", Toast.LENGTH_SHORT).also {
                        it.setGravity(Gravity.CENTER_HORIZONTAL,0,0)
                        it.show()
                    }

                } else {

                    if (item.status != "Chosen" && item.status != "Unavailable" && itemClick == 0) {
                        item.status = "Chosen"
                        item.layoutBG = R.drawable.customborder_blue
                        viewModel.pickedTimeSlot = (viewModel.itemList[position].time)
                        itemClick = 1
                        binding.buttonNext.isEnabled = true

                    } else {
                        Log.d(TAG, "$itemClick is itemClick")

                        if (item.status == "Chosen" && itemClick == 1) {
                            itemClick = 0
                            item.status = "Not booked"
                            viewModel.pickedTimeSlot = ""
                            item.layoutBG = R.drawable.customborder
                            binding.buttonNext.isEnabled = false

                        } else Toast.makeText(requireContext(),
                            "You can pick only one time slot.",
                            Toast.LENGTH_SHORT).also {
                            it.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0)
                            it.show()
                        }
                        Log.d(TAG, "$itemClick is itemClick")
                    }
                }

        Log.d(TAG, "Time slot is: ${viewModel.pickedTimeSlot} status of clicked item is ${viewModel.itemList[position].status}, itemClick is $itemClick")
        adapter.notifyItemChanged(position)

    }

}

