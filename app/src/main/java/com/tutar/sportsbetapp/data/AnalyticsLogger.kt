package com.tutar.sportsbetapp.data


import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

object AnalyticsLogger {

    fun logOddSelected(context: Context, eventName: String, oddName: String) {
        val bundle = Bundle().apply {
            putString("event_name", eventName)
            putString("odd_name", oddName)
        }
        FirebaseAnalytics.getInstance(context).logEvent("odd_selected", bundle)
    }

    fun logOddDeselected(context: Context, eventName: String, oddName: String) {
        val bundle = Bundle().apply {
            putString("event_name", eventName)
            putString("odd_name", oddName)
        }
        FirebaseAnalytics.getInstance(context).logEvent("odd_deselected", bundle)
    }

    fun logEventDetailOpened(context: Context, eventName: String) {
        val bundle = Bundle().apply {
            putString("event_name", eventName)
        }
        FirebaseAnalytics.getInstance(context).logEvent("event_detail_clicked", bundle)
    }
}
