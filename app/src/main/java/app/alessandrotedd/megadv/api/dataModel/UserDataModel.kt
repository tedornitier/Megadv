package app.alessandrotedd.megadv.api.dataModel

data class UserDataModel(
    val responseOK : Boolean,
    val id: String,
    val email: String,
    val crediti: Float
)