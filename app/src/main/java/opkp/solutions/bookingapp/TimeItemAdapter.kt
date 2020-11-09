package opkp.solutions.bookingapp


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.recyclerview_time_item.view.*
import opkp.solutions.bookingapp.time.TimeData

private const val TAG = "TimeItemAdapter"

class TimeItemAdapter(
    private val timeItemDataList: List<TimeData>,
    private val listener: OnItemClickListener,
) :
    RecyclerView.Adapter<TimeItemAdapter.TimeItemViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeItemViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_time_item, parent, false)


        return TimeItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TimeItemViewHolder, position: Int) {
        val currentItem = timeItemDataList[position]

        holder.layout.setBackgroundResource(currentItem.layoutBG)
        holder.imageView.setImageResource(currentItem.image)
        holder.timeFrame.text = currentItem.time
        holder.status.text = currentItem.status
    }

    override fun getItemCount() = timeItemDataList.size

    inner class TimeItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {


        val layout: ConstraintLayout = itemView.rv_item_layout!!
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