package com.tutar.sportsbetapp.data.mappers

fun formatOddLabel(
    outcomeName: String?,
    marketKey: String?,
    point: Double?,
    homeTeam: String?,
    awayTeam: String?
): String {
    return when (marketKey) {
        "h2h" -> {
            when (outcomeName) {
                homeTeam -> "MS 1"
                awayTeam -> "MS 2"
                "Draw" -> "MS 0"
                else -> outcomeName ?: "-"
            }
        }

        "totals" -> {
            val pointStr = point?.toString() ?: "-"
            when (outcomeName?.lowercase()) {
                "over" -> "${pointStr} Üst"
                "under" -> "${pointStr} Alt"
                else -> outcomeName ?: "-"
            }
        }

        else -> outcomeName ?: "-"
    }
}
