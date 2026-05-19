package io.github.panekd.stroopswap.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import io.github.panekd.stroopswap.data.Score
import io.github.panekd.stroopswap.data.ScoreDatabase
import io.github.panekd.stroopswap.data.ScoreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

class ScoreViewModel(
    application: Application
) : AndroidViewModel(application) {
    private val repo: ScoreRepository

    init {
        val dao = ScoreDatabase.getDatabase(application).scoreDao()
        repo = ScoreRepository(dao)
    }

    val scores = repo.scores
    val highScore = repo.highScore

    fun add(score: Int, timestamp: Long = Calendar.getInstance().timeInMillis) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.insertScore(Score(timestamp, score))
        }
    }

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteScores()
        }
    }
}