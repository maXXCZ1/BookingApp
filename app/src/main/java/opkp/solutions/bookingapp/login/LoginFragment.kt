package opkp.solutions.bookingapp.login

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
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
    private lateinit var binding: FragmentLoginBinding

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
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_login,
            container,
            false
        )


        binding.emailEditext.requestFocus()


        binding.loginButton.setOnClickListener {
            //TODO validate database account and navigate to CalendarFragment
            val email = binding.textInput2.text.toString()
            val password = binding.textInput1.text.toString()
            inputCheck(email, password)
        }

        binding.createAccountButton.setOnClickListener{

            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToCreateAccountFragment())
        }
        return binding.root
    }


    private fun inputCheck(email: String, password: String) {
        Log.d(TAG,"inputCheck started: email is $email + password is $password")

        if (email.isEmpty()) {
            binding.emailEditext.error = "Email must not be empty!"
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailEditext.error = "Invalid email format!"
        } else {
            binding.emailEditext.error = null
            binding.emailEditext.helperText = "Correct input"
        }
        if (password.isEmpty()) {
            binding.passwordEditttext.error = "Password must not be empty!"
        } else if (password.isNotEmpty() && password.length < 6) {
            binding.passwordEditttext.error = "Password has to contain at least 6 characters!"

        } else {
            binding.passwordEditttext.error = null
            binding.passwordEditttext.helperText = "Correct input"
            loginUser(email, password)
        }


    }

    private fun loginUser(email: String, password: String) {

        Log.d(TAG, "loginUser started: email is $email, password is $password")
        auth = FirebaseAuth.getInstance()

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    if (auth.currentUser!!.isEmailVerified) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail: success")
                        val user = auth.currentUser//TODO
                        updateUI(user)
                    } else {
                        Log.d(TAG, "signInWithEmail: fail, email not verified")
                        Toast.makeText(
                            activity, "Please verify your email address.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Log.d(TAG, "message")

                    val exception = task.exception as FirebaseAuthException
                    Log.d(TAG, "error code is ${exception.errorCode}")

                    when (exception.errorCode) {
                        "ERROR_USER_NOT_FOUND" ->
                          Toast.makeText(
                            activity, "Invalid email, please try again \nor create new account.",
                            Toast.LENGTH_LONG
                        ).show()

                        "ERROR_WRONG_PASSWORD" ->
                        Toast.makeText(
                            activity, "Invalid password, please try again.",
                            Toast.LENGTH_LONG
                        ).show()

                        "ERROR_EMAIL_ALREADY_IN_USE" ->
                            Toast.makeText(
                                activity, "Email address is already in use.",
                                Toast.LENGTH_LONG
                            ).show()
                        else  -> {
                            Toast.makeText(
                                activity, "Login failed: ${exception.message}.",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                    }
                }

    }


    private fun updateUI(user: FirebaseUser?) {
        Log.d(TAG, "updateUI started")
        if (user != null) {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToCalendarFragment())
            Toast.makeText(
                activity, "Login successful, welcome.",
                Toast.LENGTH_SHORT
            ).also {
                it.setGravity(Gravity.CENTER_HORIZONTAL,0,0)
                it.show()
            }

        }else {
            Toast.makeText(
                activity, "Login Failed, please try again",
                Toast.LENGTH_SHORT
            ).show()
        }

    }


    //TODO remove if you want login and create account fragments
    override fun onStart() {
        super.onStart()

        val currentUser = auth.currentUser

        if (currentUser != null && auth.currentUser!!.isEmailVerified) {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToCalendarFragment())
        }

    }

}