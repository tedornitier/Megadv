package app.alessandrotedd.megadv.activities

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import app.alessandrotedd.megadv.R

@SuppressLint("Registered")
abstract class LoadingActivity : AppCompatActivity() {

    private lateinit var loadingScreen: Dialog

    /**
     * initialize the loading screen
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // set loadingScreen as a fullscreen Dialog
        loadingScreen = Dialog(this, R.style.fullscreen)
        loadingScreen.requestWindowFeature(Window.FEATURE_NO_TITLE)
        // set loading layout
        loadingScreen.setContentView(R.layout.loading_layout)
        // avoid user interaction
        loadingScreen.setCancelable(false)
        // set background color
        loadingScreen.window?.setBackgroundDrawableResource(R.color.semiTransparent)
    }

    /**
     * disable user interaction with the UI
     */
    private fun AppCompatActivity.blockInput() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
    }

    /**
     * enable user interaction with the UI
     */
    private fun AppCompatActivity.unblockInput() {
        runOnUiThread {
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
    }

    /**
     * show/hide the loading progressBar and block/allow user interaction with the UI, depending on the loading variable value
     * @param loading true if it should start the loading process, false if it should stop it
     * @see blockInput
     * @see unblockInput
     */
    fun setLoading(loading: Boolean) {
        // start loading
        if (loading) {
            // show loadingScreen
            loadingScreen.show()
            // prevent user from using the UI
            blockInput()
        }
        // stop loading
        else {
            // dismiss diaog
            loadingScreen.dismiss()
            // allow user to interact with the UI
            unblockInput()
        }
    }
}
