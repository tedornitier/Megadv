package app.alessandrotedd.megadv.activities

import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import app.alessandrotedd.megadv.R
import app.alessandrotedd.megadv.api.API
import app.alessandrotedd.megadv.api.dataModel.ReportModel
import app.alessandrotedd.megadv.report.ReportActionItem
import app.alessandrotedd.megadv.report.ReportAdapter
import app.alessandrotedd.megadv.report.ReportDayItem
import app.alessandrotedd.megadv.report.ReportItem
import com.jakewharton.threetenabp.AndroidThreeTen
import kotlinx.android.synthetic.main.activity_report.*
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.text.DateFormatSymbols

class ReportActivity : DataLoadingActivity() {

    override val layoutResource = R.layout.activity_report
    override val titleResource = R.string.reportTitle

    /**
     * Load report data every time the activity is resumed
     */
    override fun onResume() {
        super.onResume()
        API.getData(API.Type.REPORT, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // initialize the timezone information
        AndroidThreeTen.init(this)
    }

    /**
     * Called when JSON data is loaded from the server.
     * It displays report data received as a response from the server in a RecyclerView
     * @param items a list of report data to use after the server response
     * @see ReportModel
     */
    override fun dataReady(items: List<Any>) {
        @Suppress("UNCHECKED_CAST")
        items as ArrayList<ReportModel>

        try {
            // set report list
            val reportList = ArrayList<ReportItem>()

            // set time variables
            val hourFormatter = DateTimeFormatter.ofPattern("HH:mm")
            var lastDay = ""
            val today = ZonedDateTime.ofInstant(Instant.now(), ZoneId.systemDefault())
            val yesterday = ZonedDateTime.ofInstant(
                Instant.now().minusSeconds(86400), ZoneId.systemDefault() // 86400 seconds in a day
            )

            // for every report item
            for (item in items) {
                // convert time to ZonedDateTime
                val zonedDateTime = ZonedDateTime.ofInstant(
                    Instant.ofEpochSecond(item.time),
                    ZoneId.systemDefault()
                )

                // convert time to day string
                val dayString =
                    when (zonedDateTime.dayOfMonth.toString() + zonedDateTime.month.toString() + zonedDateTime.year.toString()) {
                        // today
                        today.dayOfMonth.toString() + today.month.toString() + today.year.toString() -> getString(
                            R.string.today
                        )
                        // yesterday
                        yesterday.dayOfMonth.toString() + yesterday.month.toString() + yesterday.year.toString() -> getString(
                            R.string.yesterday
                        )
                        // dd Month
                        else -> zonedDateTime.dayOfMonth.toString() + " " +
                                DateFormatSymbols().months[zonedDateTime.monthValue - 1]
                    }

                // every new day
                if (lastDay != dayString)
                // add day block to the report list
                    reportList.add(ReportDayItem(dayString))
                lastDay = dayString

                // add action block containing all the remaining info
                val formattedHour = zonedDateTime.format(hourFormatter)
                reportList.add(ReportActionItem(item, formattedHour))
            }

            // show data in the RecyclerView
            reportRecyclerView.setHasFixedSize(true)
            reportRecyclerView.layoutManager = LinearLayoutManager(this)
            reportRecyclerView.adapter = ReportAdapter(reportList)
        } catch (e: Exception) {
            // log and show error as toast
            e.printStackTrace()
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }

        // end the loading process
        setLoading(false)
    }

}
