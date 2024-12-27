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
            ClassEntity(name = "Math", teacher = "Mr. Johnson", location = "Room 204", time = "10:00 AM"),
            ClassEntity(name = "Physics", teacher = "Ms. Smith", location = "Room 305", time = "12:00 PM"),
            ClassEntity(name = "Chemistry", teacher = "Dr. Lee", location = "Room 101", time = "2:00 PM")
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
