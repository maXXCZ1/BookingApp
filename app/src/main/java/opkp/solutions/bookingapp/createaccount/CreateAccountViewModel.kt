package opkp.solutions.bookingapp.createaccount

import android.app.Activity
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth


private const val TAG = "CreateAccountViewModel"

class CreateAccountViewModel: ViewModel(){

//    private lateinit var auth: FirebaseAuth
//
//    val passwordLivedata = MutableLiveData<String>("")
//
//
//    val _incorrectPasswordLength = MutableLiveData<Boolean>()
//        val incorrectPasswordLength: LiveData<Boolean>
//        get() = _incorrectPasswordLength
//
//    val _emptyPassword = MutableLiveData<Boolean>()
//    val emptyPassword: LiveData<Boolean>
//        get() = _emptyPassword
//
//    val _emptyEmail = MutableLiveData<Boolean>()
//    val emptyEmail: LiveData<Boolean>
//        get() = _emptyEmail
//
//    val _wrongEmailFormat = MutableLiveData<Boolean>()
//    val wrongEmailFormat: LiveData<Boolean>
//        get() = _wrongEmailFormat
//
//    val _inputCheckOK = MutableLiveData<Boolean>()
//    val inputCheckOK: LiveData<Boolean>
//        get() = _inputCheckOK
//
//
//    fun inputCheck(email: String, password: String) {
//
//        Log.d(TAG, "email is $email, password is $password")
//        _emptyEmail.value = email.isEmpty()
//        if (email.isNotEmpty()) {
//            _wrongEmailFormat.value = !Patterns.EMAIL_ADDRESS.matcher(email).matches()
//        }
//
//        _emptyPassword.value = password.isEmpty()
//        if(password.isNotEmpty() && password.length > 0 && password.length < 6) {
//                _incorrectPasswordLength.value = true
//            }
//
////            else _inputCheckOK.value = true
//    }
}