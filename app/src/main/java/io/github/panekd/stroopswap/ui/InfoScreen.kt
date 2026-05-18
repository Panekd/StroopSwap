package io.github.panekd.stroopswap.ui

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withLink
import androidx.core.net.toUri

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoScreen(toHome: () -> Unit) {
    val context = LocalContext.current

    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            TopAppBar(
                title = { Text("Info") },
                navigationIcon = {
                    IconButton(
                        onClick = toHome
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back to menu"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
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
}