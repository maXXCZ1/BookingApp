package opkp.solutions.bookingapp.court

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import opkp.solutions.bookingapp.R
import opkp.solutions.bookingapp.databinding.FragmentCourtBinding
import opkp.solutions.bookingapp.viewmodels.SharedViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CourtFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CourtFragment : Fragment(){

    private lateinit var binding: FragmentCourtBinding
    private lateinit var viewModel: SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate<FragmentCourtBinding>(inflater, R.layout.fragment_court, container, false)

        var itemClick1 = 0
        var itemClick2 = 0
        var itemClick3 = 0
        var itemClick4 = 0

       binding.imCourt1.setOnClickListener {
            onCourtClick(1, itemClick1)
            itemClick1++
       }
        binding.imCourt2.setOnClickListener {
            onCourtClick(2, itemClick2)
            itemClick2++
       }
        binding.imCourt3.setOnClickListener {
            onCourtClick(3, itemClick3)
            itemClick3++

       }
        binding.imCourt4.setOnClickListener {
            onCourtClick(4, itemClick4)
            itemClick4++

       }


        binding.buttonBook.setOnClickListener {
            //TODO register slot into database and log out user

        }

        binding.buttonPrevious2.setOnClickListener() {
            findNavController().popBackStack()
        }

        return binding.root
    }


    private fun onCourtClick (courtNo: Int, itemClick: Int) {
        if (itemClick==0 || itemClick%2 == 0) {
            when(courtNo) {
                1 -> binding.imCourt1.setImageResource(R.drawable.court1_blue)
                2 -> binding.imCourt2.setImageResource(R.drawable.court2_blue)
                3 -> binding.imCourt3.setImageResource(R.drawable.court3_blue)
                4 -> binding.imCourt4.setImageResource(R.drawable.court4_blue)
            }

        } else {
            when(courtNo) {
                1 -> binding.imCourt1.setImageResource(R.drawable.court1)
                2 -> binding.imCourt2.setImageResource(R.drawable.court2)
                3 -> binding.imCourt3.setImageResource(R.drawable.court3)
                4 -> binding.imCourt4.setImageResource(R.drawable.court4)
            }

        }

    }

}