package com.tutar.sportsbetapp.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class OddsResponse(

    @SerializedName("id")
    val id: String?,
    @SerializedName("sport_key")
    val sportKey: String?,
    @SerializedName("sport_title")
    val sportTitle: String?,
    @SerializedName("commence_time")
    val commenceTime: String?,
    @SerializedName("home_team")
    val homeTeam: String?,
    @SerializedName("away_team")
    val awayTeam: String?,
    @SerializedName("bookmakers")
    val bookmakers: List<BookMakers>?,
    var selectedOddName: String?,
    var isExpanded: Boolean = false
): Parcelable