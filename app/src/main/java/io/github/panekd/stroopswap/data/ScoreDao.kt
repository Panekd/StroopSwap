package io.github.panekd.stroopswap.data

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ScoreDao{
    @Insert
    suspend fun insert(score: Score)

    @Query("SELECT * FROM Score ORDER BY score DESC")
    fun getScores(): LiveData<List<Score>>

    @Query("SELECT * FROM Score ORDER BY score DESC LIMIT 1")
    fun getHighScore(): LiveData<Score>

    @Query("DELETE FROM Score")
    suspend fun deleteAll()

    // for use by a ContentProvider
    @Query("SELECT * FROM Score ORDER BY score DESC")
    fun cpGetScores(): Cursor

    @Query("SELECT * FROM Score WHERE timestamp = :timestamp")
    fun cpGetScoreOn(timestamp: Long): Cursor

    @Query("SELECT * FROM Score ORDER BY score DESC LIMIT 1")
    fun cpGetHighScore(): Cursor
}