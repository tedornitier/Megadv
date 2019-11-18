package app.alessandrotedd.megadv.api.dataModel

data class ReportModel(
    val time: Long,
    val action: Int,
    val crediti: Float,
    val obj: String
)