package app.alessandrotedd.megadv.activities

abstract class DataLoadingActivity : NavigationActivity() {
    /**
     * callback to implement when JSON data is loaded from the server
     * @param items a list of objects to handle after the data is loaded
     */
    abstract fun dataReady(items: List<Any>)
}
