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
import androidx.compose.ui.res.stringResource
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
                title = { Text(stringResource(R.string.information)) },
                navigationIcon = {
                    IconButton(
                        onClick = toHome
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            stringResource(R.string.to_menu)
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
            Heading(stringResource(R.string.what_is_it))
            Text(stringResource(R.string.what_is_it_description))
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
                            append(stringResource(R.string.wikilink))
                        }
                    }
                }
            )
            Heading(stringResource(R.string.how_to_play))
            Text(stringResource(R.string.two_modes))
            Text(stringResource(R.string.colour_mode_info))
            Text(stringResource(R.string.example))
            Image(painterResource(R.drawable.colour_mode_info),
                stringResource(R.string.colour_mode_img_desc))
            Text(stringResource(R.string.word_mode_info))
            Text(stringResource(R.string.example))
            Image(painterResource(R.drawable.word_mode_info),
                stringResource(R.string.word_mode_img_desc))
            Text(stringResource(R.string.mode_change_info))
            Text(stringResource(R.string.info_end))
        }
    }
}