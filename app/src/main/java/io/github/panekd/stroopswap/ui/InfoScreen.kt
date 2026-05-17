package io.github.panekd.stroopswap.ui

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withLink
import androidx.core.net.toUri

@Composable
fun InfoScreen(toHome: () -> Unit) {
    val context = LocalContext.current

    Column {
        Text("Stroop test blah blah blah")
        Text(
            buildAnnotatedString {
                withLink(
                    LinkAnnotation.Clickable(
                        tag = "wikilink",
                        linkInteractionListener = {
                            val intent = Intent(Intent.ACTION_VIEW).apply {
                                data = "https://en.wikipedia.org/wiki/Stroop_effect".toUri()
                            }
                            context.startActivity(intent)
                        }
                    )
                ) {
                    append("See Stroop effect on Wikipedia")
                }
            }
        )
    }
}