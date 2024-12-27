import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ClassDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClass(classEntity: ClassEntity)

    @Query("SELECT * FROM classes")
    fun getAllClassesLive(): LiveData<List<ClassEntity>>

    @Delete
    suspend fun deleteClass(classEntity: ClassEntity)
}
