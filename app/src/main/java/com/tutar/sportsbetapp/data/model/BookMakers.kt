package com.tutar.sportsbetapp.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class BookMakers (

    @SerializedName("key")
    val key: String?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("last_update")
    val lastUpdate: String?,
    @SerializedName("markets")
    val markets: List<Market>?
): Parcelable