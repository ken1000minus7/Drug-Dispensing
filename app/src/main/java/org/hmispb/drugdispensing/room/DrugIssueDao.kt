package org.hmispb.drugdispensing.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import org.hmispb.drugdispensing.model.DrugIssue

@Dao
interface DrugIssueDao {
    @Insert
    suspend fun insertDrugIssue(drugIssue : DrugIssue)

    @Query("SELECT * FROM drugissue")
    fun getAllDrugIssues() : LiveData<List<DrugIssue>>

    @Delete
    suspend fun deleteDrugIssue(drugIssue: DrugIssue)

    @Query("DELETE FROM drugissue")
    suspend fun deleteAllDrugIssues()

    @Query("UPDATE drugissue SET isUploaded=1 WHERE id=:id")
    suspend fun setUploaded(id : Int)

    @Query("SELECT count(*) FROM drugissue WHERE isUploaded=0")
    suspend fun notUploadedCount() : Int
}