package opkp.solutions.bookingapp.createaccount

import android.os.Bundle
import android.text.InputFilter
import android.text.Spanned
import android.util.Log
import android.util.Patterns
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import opkp.solutions.bookingapp.R
import opkp.solutions.bookingapp.databinding.FragmentCreateAccountBinding


private const val TAG = "CreateAccountFragment"

/**
 * A simple [Fragment] subclass.
 * Use the [CreateAccountFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CreateAccountFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    private lateinit var binding: FragmentCreateAccountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_create_account, container, false)

        binding.lifecycleOwner = viewLifecycleOwner


        binding.textInput4.requestFocus()

        binding.textInput3.filters = arrayOf(object : InputFilter {
            override fun filter(
                p0: CharSequence?,
                p1: Int,
                p2: Int,
                p3: Spanned?,
                p4: Int,
                p5: Int
            ): CharSequence? {
                return p0?.subSequence(p1, p2)?.replace(Regex("[^A-Za-z0-9]"), "")
            }
        })

        binding.createNewAccountButton.setOnClickListener {

            val email= binding.textInput4.text.toString()
            val password = binding.textInput3.text.toString()

            inputCheck(email, password)
        }

        return binding.root
    }


    private fun inputCheck(email: String, password: String) {

        Log.d(TAG, "inputCheck started: email is $email + password is $password")

        if (email.isEmpty()) {
            binding.createEmailLayout.error = "Email must not be empty!"
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.createEmailLayout.error = "Invalid email format!"
        } else {
            binding.createEmailLayout.error = null
            binding.createEmailLayout.helperText = "Correct input"
        }
        if (password.isEmpty()) {
            binding.createPasswordLayout.error = "Password must not be empty!"
        } else if (password.isNotEmpty() && password.length < 6) {
            binding.createPasswordLayout.error = "Password has to contain at least 6 characters!"

        } else {
            binding.createPasswordLayout.error = null
            binding.createPasswordLayout.helperText = "Correct input"
            createUser(email, password)
        }


    }

    private fun createUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "createUserWithEmail:success")
                    auth.currentUser?.sendEmailVerification()?.addOnCompleteListener(requireActivity()) { verification  ->
                        if (verification.isSuccessful) {
                            Log.d(TAG, "Verification email successfully sent.")       // Sign in success, update UI with the signed-in user's information
                            val user = auth.currentUser
                            updateUI(user)
                        } else {
                            Toast.makeText(activity, "Cannot send verification email.",
                                Toast.LENGTH_SHORT).also {
                                it.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0)
                                it.show()
                            }
                        }
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    val exception = task.exception as FirebaseAuthException
                    Log.w(TAG, "createUserWithEmail: failure", task.exception)

                    when(exception.errorCode) {
                        "ERROR_EMAIL_ALREADY_IN_USE" ->
                            Toast.makeText(activity, "Email already in use, try different please.",
                            Toast.LENGTH_SHORT).also {
                                it.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0)
                                it.show()
                            }
                        "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL" ->
                            Toast.makeText(activity, "Account already exists with same email but different sign-in credentials.",
                                Toast.LENGTH_SHORT).also {
                                it.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0)
                                it.show()
                            }
                        else ->
                            Toast.makeText(activity, "${task.exception?.message} Try again please",
                                Toast.LENGTH_SHORT).show()
                    }
                    }
            }
    }

    private fun updateUI(user: FirebaseUser?) {

        if (user != null) {
            findNavController().navigate(CreateAccountFragmentDirections.actionCreateAccountFragmentToLoginFragment())
            Toast.makeText(
                activity, "Account successfully created, verify your email address",
                Toast.LENGTH_SHORT
            ).also {
                it.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0)
                it.show()
            }
        }else {
            Toast.makeText(
                activity, "Account creation failed, please try again",
                Toast.LENGTH_SHORT
            ).also {
                it.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0)
                it.show()
            }
        }

    }
}

