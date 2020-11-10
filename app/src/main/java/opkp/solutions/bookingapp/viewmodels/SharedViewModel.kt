package opkp.solutions.bookingapp.viewmodels


import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import opkp.solutions.bookingapp.*
import opkp.solutions.bookingapp.time.TimeData
import java.text.SimpleDateFormat
import java.util.*


private const val TAG = "ShareViewModel"

class SharedViewModel : ViewModel() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
        private lateinit var currentDateFormatted: String

    var itemList = listOf<TimeData>()
    var pickedDate = ""
    var pickedTimeSlot: String = ""
    var pickedCourt = mutableListOf<Int>()
    var linearLayout = false

    var currentUserID: String? = ""

    var itemClick1 = false
    var itemClick2 = false
    var itemClick3 = false
    var itemClick4 = false
    var anyCourtClicked = 0

    var finalDate = MutableLiveData<String>()


    private var _invalidDate = MutableLiveData<Boolean>()
    val invalidDate: LiveData<Boolean>
        get() = _invalidDate

    private var _loadingState = MutableLiveData<DataState>()
    val loadingState: LiveData<DataState>
        get() = _loadingState


    private fun setCurrentDateFormatted(): String {

        val currentDate = Calendar.getInstance().time

        @SuppressLint("SimpleDateFormat")
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
        currentDateFormatted = simpleDateFormat.format(currentDate)

        return currentDateFormatted
    }


    @SuppressLint("SimpleDateFormat")
    @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    fun checkDate(date: String) {

        setCurrentDateFormatted()
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")


        if (date.isNotEmpty()) {
            _invalidDate.value =
                simpleDateFormat.parse(date).before(simpleDateFormat.parse(currentDateFormatted))
        }
    }


    fun pickDate(date: String) {

        setCurrentDateFormatted()

        if (date.isEmpty()) {
            pickedDate = currentDateFormatted
            finalDate.value = currentDateFormatted
        } else {
            pickedDate = date
            finalDate.value = date
        }
        generateTimeDataList(pickedDate)
        Log.d(TAG, "pickDate ended: saved date is $pickedDate, finalDate value is ${finalDate}")
    }


    fun generateTimeDataList(pickedDate: String): List<TimeData> {
        itemList = emptyList()
        val startHour: Int
        Log.d(
            TAG,
            "GenerateTimeDataList started: currentDayFormatted is: $currentDateFormatted, pickedDate is $pickedDate"
        )


        val calendarInstance = Calendar.getInstance()
        val currentHour = calendarInstance.get(Calendar.HOUR_OF_DAY)
        val currentMinutes = calendarInstance.get(Calendar.MINUTE)
        Log.d(TAG, "current hour is: $currentHour, current minutes are $currentMinutes")


        if (pickedDate == currentDateFormatted) {
            linearLayout = true
            if (currentHour <= 6) {
                startHour = 7
            } else {
                startHour = if (currentMinutes in 0..30) {
                    currentHour + 1
                } else {
                    currentHour + 2
                }
            }

            if (startHour > 21) {
                val item = TimeData(
                    R.drawable.ic_clock,
                    "No available time slots today, \npick another date please.",
                    "Unavailable"
                )
                itemList = itemList + item
            } else {
                linearLayout = false
                for (i in startHour until 22) {
                    val item =
                        TimeData(R.drawable.ic_clock, "$i:00 - ${i + 1}:00", "Not booked")
                    itemList = itemList + item
                }
            }
        } else {
            linearLayout = false
            startHour = 7
            for (i in startHour until 22) {
                val item =
                    TimeData(R.drawable.ic_clock, "$i:00 - ${i + 1}:00", "Not booked")
                itemList = itemList + item
            }
        }
        Log.d(
            TAG,
            "generateTimeDataList ended, list size is ${itemList.size}, first time is: ${itemList[0].time}, last element is ${itemList.last().time}"
        )

        return itemList
    }


    fun getUserID(): String {
        auth = FirebaseAuth.getInstance()
        return auth.currentUser?.uid ?: throw NullPointerException("Expression 'uid' must not be null")
    }

    fun writeNewUser() {
        database = FirebaseDatabase.getInstance().getReference("users")
        val bookingID = database.push().key!!
        val user = BookedData(pickedDate, pickedTimeSlot, pickedCourt.sorted())
        _loadingState.value = LoadingState()


        currentUserID?.let { it ->
            database.child(it).child(bookingID).setValue(user).addOnCompleteListener {
                _loadingState.value = CompletedState()
            }
                .addOnFailureListener {
                    _loadingState.value = ErrorState(it)
                }
        }


    }

    fun initializeSharedViewModel() {
        itemList = emptyList()
        pickedDate = ""
        pickedTimeSlot = ""
        pickedCourt = mutableListOf<Int>()
        linearLayout = false
        _loadingState.value = null

        itemClick1 = false
        itemClick2 = false
        itemClick3 = false
        itemClick4 = false
        anyCourtClicked = 0

    }

}
