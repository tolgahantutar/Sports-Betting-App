package com.tutar.sportsbetapp.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Market(

    @SerializedName("key")
    val key: String?,
    @SerializedName("last_update")
    val lastUpdate: String?,
    @SerializedName("outcomes")
    val outComes: List<OutCome>?
): Parcelable