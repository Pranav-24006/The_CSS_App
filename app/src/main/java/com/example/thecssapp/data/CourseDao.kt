package com.example.thecssapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.thecssapp.model.CourseItem
import kotlinx.coroutines.flow.Flow

@Dao
interface CourseDao {

    @Insert
    suspend fun addCourse(item: CourseItem)

    @Update
    suspend fun updateCourse(item: CourseItem)

    @Delete // Annotation for the delete function
    suspend fun deleteCourse(item: CourseItem)

    @Query("SELECT * FROM courses ORDER BY title ASC")
    fun getCourses(): Flow<List<CourseItem>>

    @Query("SELECT * FROM courses WHERE id = :courseId")
    fun getCourseById(courseId: Long): Flow<CourseItem>
}
