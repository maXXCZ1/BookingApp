package opkp.solutions.bookingapp

data class BookedData(val currentUserID: String = "", val finalDate: String = "", val finalTimeSlot: String = "", var finalCourts: List<Int> = emptyList())
