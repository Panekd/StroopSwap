package io.github.panekd.stroopswap.data

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri

class ScoreProvider : ContentProvider() {

    companion object {
        const val AUTHORITY = "io.github.panekd.stroopswap.data.scores"

        private const val SCORES = 1
        private const val SCORE_TIME = 2
        private const val SCORE_HIGH = 3

        private val matcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTHORITY, "score", SCORES)
            addURI(AUTHORITY, "score/#", SCORE_TIME)
            addURI(AUTHORITY, "score/high", SCORE_HIGH)
        }
    }

    private lateinit var db: ScoreDatabase
    private lateinit var dao : ScoreDao

    override fun onCreate(): Boolean {
        if (context == null) return false
        db = ScoreDatabase.getDatabase(context!!)
        dao = db.scoreDao()
        return true
    }

    override fun getType(uri: Uri): String? {
        return when (matcher.match(uri)) {
            SCORES ->
                "vnd.android.cursor.dir/vnd.$AUTHORITY.score"

            SCORE_TIME, SCORE_HIGH ->
                "vnd.android.cursor.item/vnd.$AUTHORITY.score"

            else -> throw IllegalArgumentException()
        }
    }

    override fun query(
        uri: Uri,
        projection: Array<out String?>?,
        selection: String?,
        selectionArgs: Array<out String?>?,
        sortOrder: String?
    ): Cursor? {
        val cursor =
            when (matcher.match(uri)) {
                SCORES -> dao.cpGetScores()

                SCORE_TIME -> {
                    val time = uri.lastPathSegment!!.toLong()
                    dao.cpGetScoreOn(time)
                }

                SCORE_HIGH -> dao.cpGetHighScore()
                else -> throw IllegalArgumentException()
            }

        cursor.setNotificationUri(
            context!!.contentResolver,
            uri
        )
        return cursor
    }

    override fun insert(
        uri: Uri,
        values: ContentValues?
    ): Uri? {
        throw UnsupportedOperationException()
    }

    override fun delete(
        uri: Uri,
        selection: String?,
        selectionArgs: Array<out String?>?
    ): Int {
        throw UnsupportedOperationException()
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String?>?
    ): Int {
        throw UnsupportedOperationException()
    }
}