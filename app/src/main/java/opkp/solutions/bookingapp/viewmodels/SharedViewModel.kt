package opkp.solutions.bookingapp.viewmodels

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import opkp.solutions.bookingapp.*
import opkp.solutions.bookingapp.R.drawable
import opkp.solutions.bookingapp.time.TimeData
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import java.text.SimpleDateFormat
import java.util.*
import kotlin.system.measureTimeMillis


private const val TAG = "SharedViewModel"


class SharedViewModel : ViewModel() {

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var currentDateFormatted: String

    var bookingListFromDB = mutableListOf<BookedData>()
    var mapTimetoCourts = HashMap<String, List<Int>>()
    var itemList = listOf<TimeData>()
    var pickedDate = ""
    var pickedTimeSlot: String = ""
    var pickedCourt = mutableListOf<Int>()
    var linearLayout = false
    var bookingID = ""
    var currentUserID = ""


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

    private val _connection = MutableLiveData<Boolean>()
    val connection: LiveData<Boolean>
        get() = _connection


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
                    drawable.ic_clock,
                    "No available time slots today, \npick another date please.",
                    "Unavailable"
                )
                itemList = itemList + item
            } else {
                linearLayout = false
                for (i in startHour until 22) {
                    val item =
                        TimeData(drawable.ic_clock, "$i:00 - ${i + 1}:00", "Not booked")
                    itemList = itemList + item
                }
            }
        } else {
            linearLayout = false
            startHour = 7
            for (i in startHour until 22) {
                val item =
                    TimeData(drawable.ic_clock, "$i:00 - ${i + 1}:00", "Not booked")
                itemList = itemList + item
            }
        }
        Log.d(
            TAG,
            "generateTimeDataList ended, list size is ${itemList.size}, first time is: ${itemList[0].time}, last element is ${itemList.last().time}"
        )

        return itemList
    }

    fun writeNewUser() {
        auth = FirebaseAuth.getInstance()

        currentUserID = auth.currentUser?.uid!!

        databaseReference = FirebaseDatabase.getInstance().getReference("bookings")

        val user = BookedData(currentUserID, pickedDate, pickedTimeSlot, pickedCourt.sorted())
        _loadingState.value = LoadingState()


        currentUserID.let { it ->
            databaseReference.child(bookingID).setValue(user).addOnCompleteListener {
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
        pickedCourt = mutableListOf()
        linearLayout = false
        _loadingState.value = null
        mapTimetoCourts = HashMap<String, List<Int>>()


        itemClick1 = false
        itemClick2 = false
        itemClick3 = false
        itemClick4 = false
        anyCourtClicked = 0

    }

    fun checkInternetConnection() {
        Log.d(TAG, "checkInternetConnection: started")
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val timeoutMs = 1500
                    val socket = Socket()
                    val socketAddress = InetSocketAddress("8.8.8.8", 53)

                    socket.connect(socketAddress, timeoutMs)
                    socket.close()

                    withContext(Dispatchers.Main) {
                        _connection.value = true
                    }
                } catch (ex: IOException) {
                    withContext(Main) {
                        _connection.value = false
                    }
                }
            }
        }
        Log.d(TAG, "checkInternetConnection: ended, connection is: ${_connection.value}")
    }

    fun loadBookingsFromDB() {

        databaseReference = FirebaseDatabase.getInstance().getReference("bookings")
        bookingID = databaseReference.push().key!!

        //TODO implement coroutines
        println("start job test, database reference is $databaseReference, bookingList is $bookingListFromDB")
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                bookingListFromDB.clear()
                Log.d(TAG, "onDataCange listener started: cleared bookingList size is ${bookingListFromDB.size}")
                if (snapshot.exists()) {
                    for (i in snapshot.children) {
                        val singleBookingFromDB = i.getValue(BookedData::class.java)
                        Log.d(TAG, "single booking is $singleBookingFromDB")
                        bookingListFromDB.add(singleBookingFromDB!!)
                    }
                }
                Log.d(TAG, "onDataCange listener ended: cleared bookingList is ${bookingListFromDB.size}")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, "Database error: $error")
            }

        })

        Log.d(TAG, "loadBookingsFromDB endedBooking list before checkBookedCourts is $bookingListFromDB")
    }

    fun checkBookedCourts() {
        val executionTimeOfBookedCourts = measureTimeMillis {
            val joinedList = ArrayList<Int>()
            Log.d(TAG,"checkBookedCourts started: database list is $bookingListFromDB, map is $mapTimetoCourts")
            for (i in 0 until bookingListFromDB.size) {
                if (bookingListFromDB[i].dateFromDB.contains(pickedDate)) {
                    val itemList1 = bookingListFromDB[i].courtsFromDB

                    if (mapTimetoCourts.containsKey(key = bookingListFromDB[i].timeSlotFromDB)) {
                        val courtsFromHashMapList =
                            mapTimetoCourts[bookingListFromDB[i].timeSlotFromDB]

                        joinedList.addAll(itemList1)
                        joinedList.addAll(courtsFromHashMapList!!)
                        mapTimetoCourts[bookingListFromDB[i].timeSlotFromDB] = joinedList.sorted()
                        joinedList.clear()
                    } else {
                        mapTimetoCourts[bookingListFromDB[i].timeSlotFromDB] =
                            bookingListFromDB[i].courtsFromDB
                    }

                    Log.d(TAG, "final map is $mapTimetoCourts")
                }
            }
        }
        Log.d(TAG, "processing time of checkBookedCourts: $executionTimeOfBookedCourts ms")
    }

}
