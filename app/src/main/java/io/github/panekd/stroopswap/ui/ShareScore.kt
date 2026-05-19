package io.github.panekd.stroopswap.ui

import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_SEND
import io.github.panekd.stroopswap.R
import java.util.Locale

fun shareScore(context: Context, score: Int) {
    val sendIntent: Intent = Intent().apply {
        action = ACTION_SEND
        putExtra(Intent.EXTRA_TEXT,
            String.format(
                Locale.UK,
                context.resources.getString(
                    R.string.share_score_text),
                score
            ))
        type = "text/plain"
    }

    val shareIntent = Intent.createChooser(sendIntent, null)
    context.startActivity(shareIntent)
}