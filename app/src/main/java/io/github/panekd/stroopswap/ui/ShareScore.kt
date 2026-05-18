package io.github.panekd.stroopswap.ui

import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_SEND

fun shareScore(context: Context, score: Int) {
    val sendIntent: Intent = Intent().apply {
        action = ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, "I scored $score in Stroop Swap")
        type = "text/plain"
    }

    val shareIntent = Intent.createChooser(sendIntent, null)
    context.startActivity(shareIntent)
}