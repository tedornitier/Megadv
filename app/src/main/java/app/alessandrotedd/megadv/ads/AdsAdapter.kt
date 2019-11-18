package app.alessandrotedd.megadv.ads

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import app.alessandrotedd.megadv.R
import app.alessandrotedd.megadv.api.dataModel.AdsModel
import com.jakewharton.threetenabp.AndroidThreeTen
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.util.*

class AdsAdapter(private val mList: ArrayList<AdsModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class AdsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // status
        val adStatusView: View = itemView.findViewById(R.id.adStatusCircleView)
        // title
        val adTitleTextView: TextView = itemView.findViewById(R.id.adTitleTextView)
        // URL
        val adUrlTextView: TextView = itemView.findViewById(R.id.adUrlTextView)
        // Number of impressions
        val adImpressionsTextView: TextView = itemView.findViewById(R.id.adImpressionsTextView)
        // Last impression Date
        val adLastImpressionTextView: TextView = itemView.findViewById(R.id.adLastImpressionTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.ad_layout, parent, false)
        return AdsViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = mList[position]
        holder as AdsViewHolder
        val context = holder.itemView.context

        // if the offer is 0 the ad is suspended (red circle), otherwise it's active (green circle)
        if (currentItem.offerta > 0)
            holder.adStatusView.setBackgroundResource(R.drawable.green_circle)
        else
            holder.adStatusView.setBackgroundResource(R.drawable.red_circle)
        // set the title
        holder.adTitleTextView.text = currentItem.titolo
        // set the URL
        holder.adUrlTextView.text = currentItem.url
        // set the impressions number
        holder.adImpressionsTextView.text = String.format(
            Locale.getDefault(),
            context.getString(R.string.impressionsToday),
            currentItem.totImpressions,
            currentItem.impressionsOggi
        )
        // set the last impression date
        AndroidThreeTen.init(context) // initialize the timezone information
        val lastImpression = ZonedDateTime.ofInstant(
            Instant.ofEpochSecond(currentItem.ultimaVisualizzazione),
            ZoneId.systemDefault()
        )
        val formattedLastImpressionDate = when (lastImpression.year) {
            // if the year is 1970 return "never", since this ad has never gotten an impression
            1970 -> context.getString(R.string.never)
            // otherwise return formatted date
            else -> lastImpression.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
        }
        holder.adLastImpressionTextView.text = String.format(
            Locale.getDefault(), context.getString(R.string.lastImpression), formattedLastImpressionDate
        )
    }

    override fun getItemCount(): Int {
        return mList.size
    }
}
