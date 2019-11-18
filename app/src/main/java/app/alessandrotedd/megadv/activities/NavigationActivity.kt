package app.alessandrotedd.megadv.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import app.alessandrotedd.megadv.R
import app.alessandrotedd.megadv.Session
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_navigation.*

abstract class NavigationActivity : LoadingActivity() {

    private lateinit var drawerToggle: ActionBarDrawerToggle
    abstract val layoutResource: Int
    abstract val titleResource: Int

    /**
     * set the layout and the listeners
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // set the layout
        setContentView(R.layout.activity_navigation)

        // add the child layout to the navigation layout
        navigationLayoutContainer.addView(
            layoutInflater.inflate(layoutResource, navigationLayoutContainer,false)
        )

        // set the title
        title = resources.getString(titleResource)

        // set the action bar drawer toggle
        drawerToggle = ActionBarDrawerToggle(this, activity_main, R.string.openDrawer, R.string.closeDrawer)
        activity_main.addDrawerListener(drawerToggle)
        drawerToggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // set the menu buttons listeners
        navigationView.setNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener {
            when (it.itemId) {
                // report
                R.id.navigationItemReport -> startActivity(Intent(this, ReportActivity::class.java))
                // ads
                R.id.navigationItemAds -> startActivity(Intent(this, AdsActivity::class.java))
                // facebook
                R.id.navigationItemFacebook -> startActivity(Intent(this, FacebookActivity::class.java))
                // logout
                R.id.navigationItemLogout -> Session.logout(this)

                else -> return@OnNavigationItemSelectedListener true
            }
            true
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (drawerToggle.onOptionsItemSelected(item))
            return true
        return super.onOptionsItemSelected(item)
    }

}