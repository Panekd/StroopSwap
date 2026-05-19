package io.github.panekd.stroopswap.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Score")
data class Score(
    @PrimaryKey val timestamp: Long,
    val score: Int
)