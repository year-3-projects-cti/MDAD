package com.example.taskit.database.dao

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.taskit.ClassEntity

@Dao
interface ClassDao {
    @Query("SELECT * FROM classes")
    fun getAllClassesLive(): LiveData<List<ClassEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClass(classEntity: ClassEntity)

    @Query("DELETE FROM classes")
    suspend fun deleteAllClasses()

    @Query("SELECT * FROM classes")
    fun getAllClassesCursor(): Cursor
}
