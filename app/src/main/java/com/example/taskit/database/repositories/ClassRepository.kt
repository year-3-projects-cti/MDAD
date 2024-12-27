import androidx.lifecycle.LiveData

class ClassRepository(private val classDao: ClassDao) {
    fun getAllClassesLive(): LiveData<List<ClassEntity>> {
        return classDao.getAllClassesLive()
    }

    suspend fun insertClass(classEntity: ClassEntity) {
        classDao.insertClass(classEntity)
    }

    suspend fun deleteClass(classEntity: ClassEntity) {
        classDao.deleteClass(classEntity)
    }
}
