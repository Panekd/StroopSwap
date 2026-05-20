package io.github.panekd.stroopswap

import android.content.ContentResolver
import android.content.Context
import androidx.core.net.toUri
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ScoreProviderTest {
    private lateinit var context: Context
    private lateinit var resolver: ContentResolver

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().context
        resolver = context.contentResolver
    }

    @Test
    fun testQueryAllScores() {
        val cursor = resolver.query(
            "content://io.github.panekd.stroopswap.data.scores/score".toUri(),
            null,
            null,
            null,
            null
        )

    }

    @Test
    fun testQueryHighScore() {
        val cursor = resolver.query(
            "content://io.github.panekd.stroopswap.data.scores/score/high".toUri(),
            null,
            null,
            null,
            null
        )
    }

    @Test
    fun testQueryScoreOnTime() {
        val time: Long = 0
        val cursor = resolver.query(
            "content://io.github.panekd.stroopswap.data.scores/score/$time".toUri(),
            null,
            null,
            null,
            null
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun testQueryInvalidUri() {
        resolver.query(
            "content://io.github.panekd.stroopswap.data.scores/a".toUri(),
            null,
            null,
            null,
            null
        )
    }
}