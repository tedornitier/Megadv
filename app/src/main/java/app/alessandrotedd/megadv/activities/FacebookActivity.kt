package app.alessandrotedd.megadv.activities

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import app.alessandrotedd.megadv.R
import app.alessandrotedd.megadv.api.API
import app.alessandrotedd.megadv.api.dataModel.FacebookModel
import app.alessandrotedd.megadv.facebook.FacebookAdapter
import com.jakewharton.threetenabp.AndroidThreeTen
import kotlinx.android.synthetic.main.activity_facebook.*

class FacebookActivity : DataLoadingActivity() {

    override val layoutResource = R.layout.activity_facebook
    override val titleResource = R.string.facebookTitle

    /**
     * Load facebook data every time the activity is resumed
     */
    override fun onResume() {
        super.onResume()
        API.getData(API.Type.FACEBOOK, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // initialize the timezone information
        AndroidThreeTen.init(this)
    }

    /**
     * Called when JSON data is loaded from the server.
     * It displays facebook data received as a response from the server in a RecyclerView
     * @param items a list of facebook data to use after the server response
     * @see FacebookModel
     */
    override fun dataReady(items: List<Any>) {
        try {
            // show data in the RecyclerView
            facebookRecyclerView.setHasFixedSize(true)
            facebookRecyclerView.layoutManager = LinearLayoutManager(this)
            @Suppress("UNCHECKED_CAST")
            facebookRecyclerView.adapter = FacebookAdapter(items as ArrayList<FacebookModel>)
        } catch (e: Exception) {
            // log and show error as toast
            e.printStackTrace()
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }

        // hide/show "no facebook page" warning message
        noFacebookTextView.visibility = when (items.size) {
            0 -> View.VISIBLE
            else -> View.GONE
        }

        // end the loading process
        setLoading(false)
    }

}
