package opkp.solutions.bookingapp.court

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
import opkp.solutions.bookingapp.CompletedState
import opkp.solutions.bookingapp.ErrorState
import opkp.solutions.bookingapp.LoadingState
import opkp.solutions.bookingapp.R
import opkp.solutions.bookingapp.databinding.FragmentCourtBinding
import opkp.solutions.bookingapp.viewmodels.SharedViewModel

private const val TAG = "CourtFragment"

/**
 * A simple [Fragment] subclass.
 * Use the [CourtFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CourtFragment : Fragment() {

    private lateinit var binding: FragmentCourtBinding
    private lateinit var viewModel: SharedViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_court,
            container,
            false)

        //TODO check if some of the courts are already booked in database and disable them for onclick + set grey background

        viewModel.loadingState.observe(viewLifecycleOwner, { dataState ->
            when (dataState) {
                is LoadingState -> {
                    binding.clProgressbar.visibility = View.VISIBLE
                    binding.buttonBook.isEnabled = false
                    binding.buttonPrevious2.isEnabled = false
                }
                is ErrorState -> {
                    Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).also {
                        it.setGravity(Gravity.CENTER, 0, 0)
                        it.show()
                    }
                    binding.clProgressbar.visibility = View.GONE
                    binding.buttonBook.isEnabled = true
                    binding.buttonPrevious2.isEnabled = true
                }
                is CompletedState -> {
                    findNavController().navigate(CourtFragmentDirections.actionCourtFragmentToSummaryFragment())
                }
            }
        })

        binding.imCourt1.setOnClickListener {
            if (!viewModel.itemClick1) {
                onCourtClick(1, viewModel.itemClick1)
                viewModel.itemClick1 = true
            } else {
                onCourtClick(1, viewModel.itemClick1)
                viewModel.itemClick1 = false
            }
        }
        binding.imCourt2.setOnClickListener {
            if (!viewModel.itemClick2) {
                onCourtClick(2, viewModel.itemClick2)
                viewModel.itemClick2 = true
            } else {
                onCourtClick(2, viewModel.itemClick2)
                viewModel.itemClick2 = false
            }
        }
        binding.imCourt3.setOnClickListener {
            if (!viewModel.itemClick3) {
                onCourtClick(3, viewModel.itemClick3)
                viewModel.itemClick3 = true
            } else {
                onCourtClick(3, viewModel.itemClick3)
                viewModel.itemClick3 = false
            }
        }
        binding.imCourt4.setOnClickListener {
            if (!viewModel.itemClick4) {
                onCourtClick(4, viewModel.itemClick4)
                viewModel.itemClick4 = true
            } else {
                onCourtClick(4, viewModel.itemClick4)
                viewModel.itemClick4 = false
            }

        }


        binding.buttonBook.setOnClickListener {
            viewModel.writeNewUser()
            Log.d(TAG,"buttonBook pressed: saved courtNumbers are ${viewModel.pickedCourt}, anyCourtClicked is ${viewModel.anyCourtClicked}")
        }

        binding.buttonPrevious2.setOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }

    private fun onCourtClick(courtNo: Int, itemClick: Boolean) {
        Log.d(TAG, "Time slot is: ${viewModel.pickedTimeSlot}")
        if (!itemClick) {
            viewModel.anyCourtClicked += courtNo
            when (courtNo) {
                1 -> {
                    binding.imCourt1.setImageResource(R.drawable.court1_blue)
                    viewModel.pickedCourt.add(courtNo)
                }
                2 -> {
                    binding.imCourt2.setImageResource(R.drawable.court2_blue)
                    viewModel.pickedCourt.add(courtNo)
                }
                3 -> {
                    binding.imCourt3.setImageResource(R.drawable.court3_blue)
                    viewModel.pickedCourt.add(courtNo)
                }
                4 -> {
                    binding.imCourt4.setImageResource(R.drawable.court4_blue)
                    viewModel.pickedCourt.add(courtNo)
                }
            }

        } else {
            viewModel.anyCourtClicked -= courtNo
            when (courtNo) {
                1 -> {
                    binding.imCourt1.setImageResource(R.drawable.court1)
                    viewModel.pickedCourt.remove(courtNo)
                }
                2 -> {
                    binding.imCourt2.setImageResource(R.drawable.court2)
                    viewModel.pickedCourt.remove(courtNo)
                }
                3 -> {
                    binding.imCourt3.setImageResource(R.drawable.court3)
                    viewModel.pickedCourt.remove(courtNo)
                }
                4 -> {
                    binding.imCourt4.setImageResource(R.drawable.court4)
                    viewModel.pickedCourt.remove(courtNo)
                }
            }
        }
        Log.d(TAG,
            "onCourtClick ended: picked times are: ${viewModel.pickedCourt}, courtNo is ${viewModel.anyCourtClicked}")

        binding.buttonBook.isEnabled = viewModel.anyCourtClicked > 0
    }

    override fun onResume() {
        super.onResume()
        for (i in viewModel.pickedCourt.indices) {
            when (viewModel.pickedCourt[i]) {
                1 -> {
                    binding.imCourt1.setImageResource(R.drawable.court1_blue)
                }
                2 -> binding.imCourt2.setImageResource(R.drawable.court2_blue)
                3 -> binding.imCourt3.setImageResource(R.drawable.court3_blue)
                4 -> binding.imCourt4.setImageResource(R.drawable.court4_blue)
            }
        }
        if (viewModel.anyCourtClicked > 0) {
            binding.buttonBook.isEnabled = true
        }
        Log.d(TAG,
            "onResume ended, courtNumbers are ${viewModel.pickedCourt}, itemclicks are: ${viewModel.itemClick1}, ${viewModel.itemClick2}, ${viewModel.itemClick3} and ${viewModel.itemClick4}, \n" +
                    " anyCourtClicked is: ${viewModel.anyCourtClicked}")
    }
}