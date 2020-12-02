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
import kotlinx.coroutines.withContext
import opkp.solutions.bookingapp.*
import opkp.solutions.bookingapp.R.drawable
import opkp.solutions.bookingapp.time.TimeData
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import java.text.SimpleDateFormat
import java.util.*


private const val TAG = "SharedViewModel"


class SharedViewModel : ViewModel() {

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var currentDateFormatted: String

    var bookingListFromDB = mutableListOf<BookedData>()
    var mapTimeToCourts = HashMap<String, List<Int>>()
    var itemList = listOf<TimeData>()
    var pickedDate: String = ""
    var pickedTimeSlot: String = ""
    var pickedCourt = mutableListOf<Int>()
    var linearLayout = false
    var bookingID = ""
    var currentUserID = ""
    var filteredList = mutableListOf<BookedData>()
    var savedPosition = -1
    val keyList = mutableListOf<String>()

    var itemClick1 = false
    var itemClick2 = false
    var itemClick3 = false
    var itemClick4 = false
    var anyCourtClicked = 0

    var finalDate = MutableLiveData<String>()

    private var _invalidDate = MutableLiveData<Boolean>()
    val invalidDate: LiveData<Boolean> = _invalidDate


    private var _loadingState = MutableLiveData<DataState>()
    val loadingState: LiveData<DataState> = _loadingState

    private var _connection = MutableLiveData<Boolean>()
    val connection: LiveData<Boolean> = _connection


    private var _dataLoadCompleted = MutableLiveData<Boolean>()
    val dataLoadCompleted: LiveData<Boolean> = _dataLoadCompleted

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

    }

    private fun generateTimeDataList(pickedDate: String): List<TimeData> {
        itemList = emptyList()

        val startHour: Int
        val calendarInstance = Calendar.getInstance()
        val currentHour = calendarInstance.get(Calendar.HOUR_OF_DAY)
        val currentMinutes = calendarInstance.get(Calendar.MINUTE)

        if (pickedDate == currentDateFormatted) {
            linearLayout = true
            startHour = if (currentHour <= 6) {
                7
            } else {
                if (currentMinutes in 0..30) {
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

        return itemList
    }

    fun writeNewUser() {
        auth = FirebaseAuth.getInstance()
        bookingID = databaseReference.push().key!!

        currentUserID = auth.currentUser?.uid!!

        databaseReference = FirebaseDatabase.getInstance().getReference("bookings")

        val user = BookedData(currentUserID, pickedDate, pickedTimeSlot, pickedCourt.sorted())
        _loadingState.value = LoadingState()

//
//        currentUserID.let {
        databaseReference.child(bookingID).setValue(user).addOnCompleteListener {
            _loadingState.value = CompletedState()
        }
            .addOnFailureListener {
                _loadingState.value = ErrorState(it)
            }
//        }
    }

    fun initializeSharedViewModel() {
        itemList = emptyList()
        pickedDate = ""
        pickedTimeSlot = ""
        pickedCourt = mutableListOf()
        linearLayout = false
        _loadingState.value = null
        mapTimeToCourts = HashMap<String, List<Int>>()
        _dataLoadCompleted.value = false


        itemClick1 = false
        itemClick2 = false
        itemClick3 = false
        itemClick4 = false
        anyCourtClicked = 0

    }

    fun checkInternetConnection() {

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val timeoutMs = 1500
                    val socket = Socket()
                    val socketAddress = InetSocketAddress("8.8.8.8", 53)

                    socket.connect(socketAddress, timeoutMs)
                    socket.close()

                    withContext(Main) {
                        _connection.value = true
                    }
                } catch (ex: IOException) {
                    withContext(Main) {
                        _connection.value = false
                    }
                }
            }
        }
    }

    fun loadBookingsFromDB() {
        _dataLoadCompleted.value = false
        databaseReference = FirebaseDatabase.getInstance().getReference("bookings")

        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                bookingListFromDB.clear()

                if (snapshot.exists()) {
                    for (i in snapshot.children) {
                        val singleBookingFromDB = i.getValue(BookedData::class.java)
                        Log.d(TAG, "single booking is $singleBookingFromDB")
                        bookingListFromDB.add(singleBookingFromDB!!)
                    }
                }
                _dataLoadCompleted.value = true

            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, "Database error: $error")
            }

        })
    }

    fun checkBookedCourts() {

        val joinedList = ArrayList<Int>()

        for (i in 0 until bookingListFromDB.size) {
            if (bookingListFromDB[i].dateFromDB.contains(pickedDate)) {
                val itemList1 = bookingListFromDB[i].courtsFromDB

                if (mapTimeToCourts.containsKey(key = bookingListFromDB[i].timeSlotFromDB)) {
                    val courtsFromHashMapList = mapTimeToCourts[bookingListFromDB[i].timeSlotFromDB]

                    joinedList.addAll(itemList1)
                    joinedList.addAll(courtsFromHashMapList!!)
                    mapTimeToCourts[bookingListFromDB[i].timeSlotFromDB] = joinedList.sorted()
                    joinedList.clear()
                } else {
                    mapTimeToCourts[bookingListFromDB[i].timeSlotFromDB] =
                        bookingListFromDB[i].courtsFromDB
                }
            }
        }
    }

    fun resetDataLoadState() {
        _dataLoadCompleted.value = false
    }

    fun filterBookingList() {

        for (booking in bookingListFromDB) {
            if (booking.currentUserID == currentUserID) {
                filteredList.add(booking)
            }
        }
    }

    fun deleteBookingFromDB() {

        val position = savedPosition
        keyList.clear()
        databaseReference.orderByChild("currentUserID").equalTo(currentUserID)

            .addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (snap in snapshot.children) {

                            val user = snap.getValue(BookedData::class.java)

                            val currentKey = snap.key!!

                            keyList.add(currentKey)
                        }

                    }

                    databaseReference.child(keyList[position]).removeValue()

                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e(TAG, "deleteBookingFromDB cancelled")
                }
            })
    }

}

