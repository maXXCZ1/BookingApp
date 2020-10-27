package opkp.solutions.bookingapp.time


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        adapter = TimeItemAdapter(viewModel.itemList, this)

        binding.timeitemRecyclerview.adapter = adapter
        if(viewModel.linearLayout) {
            binding.timeitemRecyclerview.layoutManager = LinearLayoutManager(requireActivity())
//            viewModel.itemList.
        } else {
            binding.timeitemRecyclerview.layoutManager = GridLayoutManager(requireActivity(), 2)
        }
        binding.timeitemRecyclerview.setHasFixedSize(true)

        binding.buttonPrevious.setOnClickListener {
            findNavController().navigate(TimeFragmentDirections.actionTimeFragmentToCalendarFragment())
        }

        if(viewModel.itemList.size == 1) {
            binding.buttonNext.isEnabled = false

        }
        binding.buttonNext.setOnClickListener {

            findNavController().navigate(TimeFragmentDirections.actionTimeFragmentToCourtFragment())
        }

        return binding.root
    }

    override fun onItemClick(position: Int) {

        val item = viewModel.itemList[position]
        if(item.status == "Unavailable") {
            binding.timeitemRecyclerview.isEnabled = false
        }

        if (item.status != "Chosen") {
            item.status = "Chosen"
            item.layoutBG = R.drawable.customborder_red

            viewModel.itemList[position].status = "Chosen"

        } else {
            item.status = "Not Booked"
            item.layoutBG = R.drawable.customborder
        }
        Log.d(TAG, "status of clicked item is ${viewModel.itemList[position].status}")
        adapter.notifyItemChanged(position)

    }
}