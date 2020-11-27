package opkp.solutions.bookingapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.recyclerview_myreservations_item.view.*

private const val TAG = "MyReserVationAdapter"

class MyReservationItemAdapter(private val bookingList: MutableList<BookedData>, private val currentUserID: String, private val listener: OnMyReservationItemClickListener) :
    RecyclerView.Adapter<MyReservationItemAdapter.ViewHolder>() {

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.recyclerview_myreservations_item, viewGroup, false)


        return ViewHolder(view)
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
   inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val listImage: ImageView = view.im_list
        val layout: ConstraintLayout = view.rv_managereservations_item_layout
        val time: TextView = view.tv_managereservations_timeslot
        val date: TextView = view.tv_managereservations_date
        val courts: TextView = view.tv_managereservations_courts

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            val currentView = itemView
            if(position != RecyclerView.NO_POSITION) {
                listener.onItemClick(currentView, position)
            }
        }
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val currentItem = bookingList[position]

        if(bookingList[position].currentUserID == currentUserID) {

            val date = "Date: \t\t\t\t${currentItem.dateFromDB}"
            val timeSlot = "Time: \t\t\t\t${currentItem.timeSlotFromDB}"
            val courts = "Court(s): \t${currentItem.courtsFromDB}"
                .replace("[", "")
                .replace("]", "")

            viewHolder.listImage.setImageResource(R.drawable.ic_list)
            viewHolder.date.text = date
            viewHolder.time.text = timeSlot
            viewHolder.courts.text = courts
        }
        else {
            val errorMessage = "Error loading data from Database"
            viewHolder.listImage.setImageResource(R.drawable.ic_list)
            viewHolder.date.text = errorMessage
            viewHolder.time.text = ""
            viewHolder.courts.text = ""
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = bookingList.size

    interface OnMyReservationItemClickListener{
        fun onItemClick(view: View, position: Int)
    }
}
