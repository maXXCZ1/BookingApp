package opkp.solutions.bookingapp

import android.content.Context
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.recyclerview_myreservations_item.view.*

private const val TAG = "MyReserVationAdapter"



class MyReservationItemAdapter(
    private val filteredList: MutableList<BookedData>,
    private val currentUserID: String,
    private val context: Context,
    private var savedPosition: Int,
    private var isClicked : Boolean,
    private val listener: OnMyReservationItemClickListener)
    : RecyclerView.Adapter<MyReservationItemAdapter.ViewHolder>() {

//    var savedPosition = -1
//    var isClicked = false


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.recyclerview_myreservations_item, viewGroup, false)


        Log.d(TAG, "onCreateViewHolder started: isClicked is $isClicked, savedPosition is $savedPosition")

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


        override fun onClick(v: View) {

            Log.d(TAG, "onClick started: isClicked is $isClicked, is $savedPosition")
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                if (!isClicked && savedPosition == -1) {
                    layout.setBackgroundResource(R.drawable.customborder_blue)
                    isClicked = true
                    savedPosition = position
                    listener.onItemClick(position)
                } else if (isClicked && savedPosition == position) {
                    layout.setBackgroundResource(R.drawable.customborder)
                    isClicked = false
                    savedPosition = -1
                    listener.onItemClick(position)
                } else {
                    Toast.makeText(context, "Can't choose multiple bookings", Toast.LENGTH_SHORT)
                        .also {
                            it.setGravity(Gravity.CENTER, 0, 0)
                            it.show()
                        }
//                    listener.onItemClick(position)
                }
                Log.d(TAG,
                    "onClick ends: isClicked is $isClicked, clicked position is $position, savedPoisiton is $savedPosition")
            }
        }

    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        Log.d(TAG, "onBindViewHolder started: isClicked is $isClicked, savedPosition is $savedPosition")

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val currentItem = filteredList[position]

        if(filteredList[position].currentUserID == currentUserID) {

            val date = "Date: \t\t\t\t${currentItem.dateFromDB}"
            val timeSlot = "Time: \t\t\t\t${currentItem.timeSlotFromDB}"
            val courts = "Court(s): \t${currentItem.courtsFromDB}"
                .replace("[", "")
                .replace("]", "")

            if(isClicked && position == savedPosition ){
                viewHolder.layout.setBackgroundResource(R.drawable.customborder_blue)
            } else {
                viewHolder.layout.setBackgroundResource(R.drawable.customborder)
            }

            viewHolder.listImage.setImageResource(R.drawable.ic_list)
            viewHolder.date.text = date
            viewHolder.time.text = timeSlot
            viewHolder.courts.text = courts
        }
        else {
            val errorMessage = "Error loading data from Database"
            viewHolder.layout.setBackgroundResource(R.drawable.customborder)
            viewHolder.listImage.setImageResource(R.drawable.ic_list)
            viewHolder.date.text = errorMessage
            viewHolder.time.text = ""
            viewHolder.courts.text = ""
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = filteredList.size

    interface OnMyReservationItemClickListener{
        fun onItemClick(position: Int)
    }
}
