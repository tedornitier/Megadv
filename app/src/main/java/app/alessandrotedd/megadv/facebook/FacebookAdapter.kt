package app.alessandrotedd.megadv.facebook

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.alessandrotedd.megadv.R
import app.alessandrotedd.megadv.api.dataModel.FacebookModel
import com.jakewharton.threetenabp.AndroidThreeTen
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.util.*

class FacebookAdapter(private val mList: ArrayList<FacebookModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class FacebookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // status
        val facebookStatusView: View = itemView.findViewById(R.id.facebookStatusCircleView)
        // title
        val facebookTitleTextView: TextView = itemView.findViewById(R.id.facebookTitleTextView)
        // URL
        val facebookUrlTextView: TextView = itemView.findViewById(R.id.facebookUrlTextView)
        // Number of likes
        val facebookLikesTextView: TextView = itemView.findViewById(R.id.facebookLikesTextView)
        // Last like date
        val facebookLastLikeTextView: TextView =
            itemView.findViewById(R.id.facebookLastLikeTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.facebook_layout, parent, false)
        return FacebookViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = mList[position]
        holder as FacebookViewHolder
        val context = holder.itemView.context

        // if the offer is 0 the facebook page is suspended (red circle), otherwise it's active (green circle)
        if (currentItem.offerta > 0)
            holder.facebookStatusView.setBackgroundResource(R.drawable.green_circle)
        else
            holder.facebookStatusView.setBackgroundResource(R.drawable.red_circle)
        // set the title
        holder.facebookTitleTextView.text = currentItem.titolo
        // set the URL
        holder.facebookUrlTextView.text = currentItem.url
        // set the likes number
        holder.facebookLikesTextView.text = String.format(
            Locale.getDefault(),
            context.getString(R.string.likesToday),
            currentItem.totLikes,
            currentItem.likesOggi
        )
        // set the last like date
        AndroidThreeTen.init(context) // initialize the timezone information
        val lastLike = ZonedDateTime.ofInstant(
            Instant.ofEpochSecond(currentItem.ultimoLike),
            ZoneId.systemDefault()
        )
        val formattedLastLikeDate = when (lastLike.year) {
            // if the year is 1970 return "never", since this facebook page has never gotten a like
            1970 -> context.getString(R.string.never)
            // otherwise return formatted date
            else -> lastLike.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
        }
        holder.facebookLastLikeTextView.text = String.format(
            Locale.getDefault(), context.getString(R.string.lastLike), formattedLastLikeDate
        )
    }

    override fun getItemCount(): Int {
        return mList.size
    }

}
