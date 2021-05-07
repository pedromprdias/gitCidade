package ipvc.estg.cidadeinteligente.api

data class ReportOutpost (
    val id: Int,
    val title: String,
    val user_name: String,
    val lat: Double,
    val lng: Double,
    val description: String,
    var photo_name: String,
    val type: String
)