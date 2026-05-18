package io.github.panekd.stroopswap.ui

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import io.github.panekd.stroopswap.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoScreen(toHome: () -> Unit) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

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
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp, 16.dp)
                .verticalScroll(scrollState, true),
        ) {
            Heading("What is the Stroop test?")
            Text("The Stroop test is a test of selective attention. " +
                "Traditionally, subjects are shown a colour word written in" +
                " a different colour font, and must respond with the colour" +
                " of the font.")
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
                        withStyle(
                            SpanStyle(
                                color = MaterialTheme.colorScheme.secondary,
                                textDecoration = TextDecoration.Underline
                            )
                        ) {
                            append("Read more about the Stroop effect on Wikipedia")
                        }
                    }
                }
            )
            Heading("How to Play")
            Text("Stroop Swap has two modes.")
            Text("The first mode, Colour Mode, is like the traditional" +
                " Stroop test. You should select the colour of the font.")
            Text("Here's an example:")
            Image(painterResource(R.drawable.colour_mode_info),
                "In colour mode, given the word RED in blue text, you should choose blue")
            Text("The other mode, Colour Mode, is the opposite. You " +
                    "should select the colour that corresponds to the meaning" +
                    " of the word.")
            Text("Here's an example:")
            Image(painterResource(R.drawable.word_mode_info),
                "In word mode, given the word BLUE in red text, you should choose red")
            Text("The game will switch between the two modes at random." +
                    " Your goal is to last as long as possible while answering" +
                    " as quickly as possible. Good luck!")
        }
    }
}