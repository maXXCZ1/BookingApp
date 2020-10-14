package opkp.solutions.bookingapp.login

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import opkp.solutions.bookingapp.R
import opkp.solutions.bookingapp.databinding.FragmentLoginBinding


private const val TAG = "LoginFragment"
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

        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)


        binding.loginButton.setOnClickListener {
            //TODO validate database account and navigate to CalendarFragment
            val email = binding.textInput2.text.toString()
            val password = binding.textInput1.text.toString()
            loginUser(email, password)
        }

        binding.createAccountButton.setOnClickListener{

            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToCreateAccountFragment())
        }
        return binding.root
    }


    private fun loginUser(email: String, password: String) {
        auth = FirebaseAuth.getInstance()

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser //TODO
                    findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToCalendarFragment())
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(activity, "Sigin failed, try again.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

//    override fun onStart() {
//        super.onStart()
//
//        val currentUser = auth.currentUser
//
//        if (currentUser != null) {
//            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToCalendarFragment())
//        }
//    }

}