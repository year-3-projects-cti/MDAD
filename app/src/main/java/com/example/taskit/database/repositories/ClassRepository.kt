package com.example.taskit.database.repository

import androidx.lifecycle.LiveData
import com.example.taskit.ClassEntity
import com.example.taskit.database.dao.ClassDao

class ClassRepository(private val classDao: ClassDao) {
    fun getAllClassesLive(): LiveData<List<ClassEntity>> {
        return classDao.getAllClassesLive()
    }

    suspend fun insertClass(classEntity: ClassEntity) {
        classDao.insertClass(classEntity)
    }

    suspend fun deleteAllClasses() {
        classDao.deleteAllClasses()
    }
}
