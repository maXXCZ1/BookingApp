package opkp.solutions.bookingapp.managereservations

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import opkp.solutions.bookingapp.R


/**
 * A simple [Fragment] subclass.
 * Use the [ManageReservationsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ManageReservationsFragment : Fragment() {


    //TODO show all my reservations as RecyclerView and let user to cancel chosen one

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_manage_reservations, container, false)
    }

}