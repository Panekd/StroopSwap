package io.github.panekd.stroopswap.data

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
}