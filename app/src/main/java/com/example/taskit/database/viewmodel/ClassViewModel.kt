package com.example.taskit.database.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.taskit.ClassEntity
import com.example.taskit.database.AppDatabase
import com.example.taskit.database.repository.ClassRepository
import kotlinx.coroutines.launch

class ClassViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ClassRepository
    val allClasses: LiveData<List<ClassEntity>>

    init {
        val classDao = AppDatabase.getDatabase(application).classDao()
        repository = ClassRepository(classDao)
        allClasses = repository.getAllClassesLive()
    }

    fun populateSampleData() {
        val sampleData = listOf(
            ClassEntity(0, "Course", "Mobile Development", "2021-10-01 08:00", "C1", "Teacher 1", "Homework 1", "2021-10-02", "Notes 1"),
            ClassEntity(1, "Lab", "Mobile Development Lab", "2021-10-01 10:00", "L1", "Teacher 2", "Homework 2", "2021-10-03", "Notes 2"),
            ClassEntity(2, "Seminar", "Mobile Development Seminar", "2021-10-01 12:00", "S1", "Teacher 3", "Homework 3", "2021-10-04", "Notes 3"),
            ClassEntity(3, "Other", "Mobile Development Other", "2021-10-01 14:00", "O1", "Teacher 4", "Homework 4", "2021-10-05", "Notes 4")
        )

        sampleData.forEach { classEntity ->
            viewModelScope.launch {
                repository.insertClass(classEntity)
            }
        }
    }

    fun insertClass(newClass: ClassEntity) {
        viewModelScope.launch {
            repository.insertClass(newClass)
        }
    }

    fun emptyClasses() {
        viewModelScope.launch {
            repository.deleteAllClasses()
        }
    }

}
