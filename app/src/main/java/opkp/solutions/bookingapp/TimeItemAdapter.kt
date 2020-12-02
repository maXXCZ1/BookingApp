package opkp.solutions.bookingapp


import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.recyclerview_time_item.view.*
import opkp.solutions.bookingapp.time.TimeData
import java.util.*


private const val TAG = "TimeItemAdapter"

class TimeItemAdapter(
    private val timeItemDataList: List<TimeData>,
    private val listener: OnItemClickListener,
    private val context: Context,
    private val map: HashMap<String, List<Int>>,
) :
    RecyclerView.Adapter<TimeItemAdapter.TimeItemViewHolder>() {

    private var currentMap = hashMapOf<String, List<Int>>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeItemViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_time_item, parent, false)

        return TimeItemViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: TimeItemViewHolder, position: Int) {
        val currentItem = timeItemDataList[position]

        currentMap = map
        Log.d(TAG, "currentMap size is ${currentMap.size}")
        val fullListOfBookedCourts = listOf(1, 2, 3, 4)
        val timeSlotFromMap = currentMap[currentItem.time]

        if (timeSlotFromMap == fullListOfBookedCourts) {
            holder.itemView.isEnabled = false
            holder.layout.setBackgroundResource(R.drawable.customborder_grey)
            holder.imageView.setImageResource(currentItem.image)
            holder.timeFrame.text = currentItem.time
            holder.status.text = context.getString(R.string.status_booked)
        } else {
            holder.layout.setBackgroundResource(currentItem.layoutBG)
            holder.imageView.setImageResource(currentItem.image)
            holder.timeFrame.text = currentItem.time
            holder.status.text = currentItem.status
        }
    }

    override fun getItemCount() = timeItemDataList.size

    inner class TimeItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        val layout: ConstraintLayout = itemView.rv_managereservations_item_layout!!
        val imageView = itemView.im_clock!!
        val timeFrame: TextView = itemView.tv_timeframe
        val status: TextView = itemView.tv_status

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

}