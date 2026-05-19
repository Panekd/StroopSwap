package io.github.panekd.stroopswap.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Score::class], version = 2, exportSchema = false)
abstract class ScoreDatabase: RoomDatabase(){
    abstract fun scoreDao(): ScoreDao
    companion object {
        @Volatile
        private var Instance: ScoreDatabase? = null
        fun getDatabase(context: Context) : ScoreDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, ScoreDatabase::class.java, "score")
                    .build().also { Instance = it }
            }
        }
    }
}