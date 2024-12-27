import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ClassViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ClassRepository
    val allClasses: LiveData<List<ClassEntity>>

    init {
        val classDao = AppDatabase.getDatabase(application).classDao()
        repository = ClassRepository(classDao)
        allClasses = repository.getAllClassesLive()
    }

    fun insertClass(classEntity: ClassEntity) {
        viewModelScope.launch {
            repository.insertClass(classEntity)
        }
    }

    fun deleteClass(classEntity: ClassEntity) {
        viewModelScope.launch {
            repository.deleteClass(classEntity)
        }
    }
}
