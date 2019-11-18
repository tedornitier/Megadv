package app.alessandrotedd.megadv.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent.KEYCODE_ENTER
import android.view.Window
import android.view.inputmethod.EditorInfo
import androidx.core.content.ContextCompat
import app.alessandrotedd.megadv.R
import app.alessandrotedd.megadv.api.API
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : LoadingActivity() {

    /**
     * Check if the user is already logged in every time the activity is resumed.
     * If the user is logged in, go to ReportActivity
     */
    override fun onResume() {
        super.onResume()
        // if there's a valid session key stored, try to use it to login in
        API.asyncCheckIfUserIsLoggedIn(this)
    }

    /**
     * Load the login layout, set the listeners and the background
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        // show login activity
        super.onCreate(savedInstanceState)
        // hide the title bar and show activity layout
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        setContentView(R.layout.activity_login)

        // set activity background
        window.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.background_img))

        // set login button click listener
        loginButton.setOnClickListener {
            // log in using email and password
            API.login(emailInput.text.toString(), passwordInput.text.toString(), this)
        }

        // set sign up link
        signupLink.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse("https://www.megadv.it/gh_backend/register.php")
            startActivity(i)
        }

        // when the user presses ENTER key after typing the password, click on the login button
        passwordInput.setOnEditorActionListener { _, actionId, event ->
            if (event != null && event.keyCode == KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE)
                loginButton.callOnClick()
            false
        }
    }
}
