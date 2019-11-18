package app.alessandrotedd.megadv.report

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.alessandrotedd.megadv.R
import java.util.*

class ReportAdapter(private val mList: ArrayList<ReportItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_REPORT_DAY = 0
        const val TYPE_REPORT_ACTION = 1
    }

    internal inner class DayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // day textView
        val mDayTextView: TextView = itemView.findViewById(R.id.dayTextView)
    }

    internal inner class ActionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // action icon
        val mActionIconView: ImageView = itemView.findViewById(R.id.actionIconView)
        // action name
        val mActionTextView: TextView = itemView.findViewById(R.id.actionTextView)
        // formatted time
        val mTimeTextView: TextView = itemView.findViewById(R.id.timeTextView)
        // icon background
        val mIconCircle: LinearLayout = itemView.findViewById(R.id.iconCircleView)
    }

    override fun getItemViewType(position: Int): Int {
        return if (mList[position] is ReportDayItem)
            TYPE_REPORT_DAY // ReportDayItem
        else
            TYPE_REPORT_ACTION // ReportActionItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            // inflate action
            TYPE_REPORT_ACTION -> {
                val v = LayoutInflater.from(parent.context).inflate(R.layout.report_action, parent, false)
                ActionViewHolder(v)
            }
            // inflate day
            TYPE_REPORT_DAY -> {
                val v = LayoutInflater.from(parent.context).inflate(R.layout.report_day, parent, false)
                DayViewHolder(v)
            }
            // error: invalid viewType
            else -> {
                Log.e("ReportAdapter", "Invalid viewType: $viewType")
                val v = LayoutInflater.from(parent.context).inflate(R.layout.report_action, parent, false)
                ActionViewHolder(v)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = mList[position]

        // set day item
        if (getItemViewType(position) == TYPE_REPORT_DAY) {
            currentItem as ReportDayItem
            holder as DayViewHolder
            holder.mDayTextView.text = currentItem.formattedDay
        }
        // set action item
        else if (getItemViewType(position) == TYPE_REPORT_ACTION) {
            currentItem as ReportActionItem
            holder as ActionViewHolder
            holder.mActionIconView.setImageResource(currentItem.iconResource)
            holder.mActionTextView.text = currentItem.action
            holder.mTimeTextView.text = currentItem.time
            holder.mIconCircle.setBackgroundResource(currentItem.iconBackgroundResource)
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }
}
