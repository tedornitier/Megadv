package app.alessandrotedd.megadv.api.dataModel

data class AdsModel(
    val titolo: String,
    val url: String,
    val offerta: Int,
    val totImpressions: Int,
    val totClicks: Int,
    val totClickNulli: Int,
    val impressionsOggi: Int,
    val clickOggi: Int,
    val clickNulliOggi: Int,
    val ultimaVisualizzazione: Long
)