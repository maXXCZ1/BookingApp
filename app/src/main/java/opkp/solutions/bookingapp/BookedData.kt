package opkp.solutions.bookingapp

data class BookedData(val currentUserID: String = "", val dateFromDB: String = "", val timeSlotFromDB: String = "", var courtsFromDB: List<Int> = emptyList())
