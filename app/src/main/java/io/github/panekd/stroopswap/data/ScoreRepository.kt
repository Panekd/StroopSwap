package io.github.panekd.stroopswap.data

import androidx.lifecycle.LiveData

class ScoreRepository(private val scoreDao: ScoreDao) {
    val scores: LiveData<List<Score>> = scoreDao.getScores()
    val highScore: LiveData<Score> = scoreDao.getHighScore()
    suspend fun insertScore(score: Score){
        scoreDao.insert(score)
    }
    suspend fun deleteScores(){
        scoreDao.deleteAll()
    }
}