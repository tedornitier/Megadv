package app.alessandrotedd.megadv.api.dataModel

data class FacebookModel(
    val titolo: String,
    val url: String,
    val offerta: Int,
    val totLikes: Int,
    val likesOggi: Int,
    val ultimoLike: Long
)