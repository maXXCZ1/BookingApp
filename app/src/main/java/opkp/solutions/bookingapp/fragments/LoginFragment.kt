package opkp.solutions.bookingapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import opkp.solutions.bookingapp.R
import opkp.solutions.bookingapp.databinding.FragmentLoginBinding
import opkp.solutions.bookingapp.viewmodels.LoginViewModel


/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private var currentUser: FirebaseUser? = null
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_login, container, false)
        val binding = DataBindingUtil.inflate<FragmentLoginBinding>(
            inflater,
            R.layout.fragment_login,
            container,
            false
        )

        binding.loginButton.setOnClickListener {
            //TODO validate database account and navigate to CalendarFragment
            val email = binding.emailEditext.editText.toString()
            val password = binding.passwordEditttext.editText.toString()
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToCalendarFragment())
        }

        binding.createAccountButton.setOnClickListener{
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToCreateAccountFragment())
        }
        return binding.root
    }


    override fun onStart() {
        super.onStart()

        val currentUser = auth.currentUser

        if (currentUser != null) {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToCalendarFragment())
        }
    }
}