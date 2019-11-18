package app.alessandrotedd.megadv.activities

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import app.alessandrotedd.megadv.R
import app.alessandrotedd.megadv.ads.AdsAdapter
import app.alessandrotedd.megadv.api.API
import app.alessandrotedd.megadv.api.dataModel.AdsModel
import com.jakewharton.threetenabp.AndroidThreeTen
import kotlinx.android.synthetic.main.activity_ads.*

class AdsActivity : DataLoadingActivity() {

    override val layoutResource = R.layout.activity_ads
    override val titleResource = R.string.adsTitle

    /**
     * Load ads data every time the activity is resumed
     */
    override fun onResume() {
        super.onResume()
        API.getData(API.Type.ADS, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // initialize the timezone information
        AndroidThreeTen.init(this)
    }

    /**
     * Called when JSON data is loaded from the server.
     * It displays ads data received as a response from the server in a RecyclerView
     * @param items a list of ads data to use after the server response
     * @see AdsModel
     */
    override fun dataReady(items: List<Any>) {
        try {
            // show data in the RecyclerView
            adsRecyclerView.setHasFixedSize(true)
            adsRecyclerView.layoutManager = LinearLayoutManager(this)
            @Suppress("UNCHECKED_CAST")
            adsRecyclerView.adapter = AdsAdapter(items as ArrayList<AdsModel>)
        } catch (e: Exception) {
            // log and show error as toast
            e.printStackTrace()
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }

        // hide/show "no ads" warning message
        noAdsTextView.visibility = when (items.size) {
            0 -> VISIBLE
            else -> GONE
        }

        // end the loading process
        setLoading(false)
    }

}
