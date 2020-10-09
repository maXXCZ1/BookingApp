package opkp.solutions.bookingapp.fragments

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import opkp.solutions.bookingapp.R
import opkp.solutions.bookingapp.databinding.FragmentCreateAccountBinding
import opkp.solutions.bookingapp.viewmodels.CreateAccountViewModel

private const val TAG = "CreateAccountFragment"

/**
 * A simple [Fragment] subclass.
 * Use the [CreateAccountFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CreateAccountFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var viewModel: CreateAccountViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentCreateAccountBinding>(inflater, R.layout.fragment_create_account, container, false)

        viewModel = ViewModelProvider(this).get(CreateAccountViewModel::class.java)



        binding.createNewAccountButton.setOnClickListener {
            val email= binding.textInput4.text.toString()
            val password = binding.textInput3.text.toString()
            Log.d(opkp.solutions.bookingapp.fragments.TAG, "email is $email, password is $password")
            registerUser(email, password)
        }
        return binding.root
    }

    private fun registerUser(email: String, password: String) {

        auth = FirebaseAuth.getInstance()

        Log.d(opkp.solutions.bookingapp.fragments.TAG, "email is $email, password is $password")

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    findNavController().navigate(CreateAccountFragmentDirections.actionCreateAccountFragmentToLoginFragment())

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(requireActivity(), "Creation failed. Try again please",
                        Toast.LENGTH_SHORT).show()
                    // TODO
                }


            }
    }

}