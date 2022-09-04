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
    fun insertDrugIssue(drugIssue : DrugIssue)

    @Query("SELECT * FROM drugissue")
    fun getAllDrugIssues() : LiveData<List<DrugIssue>>

    @Delete
    fun deleteDrugIssue(drugIssue: DrugIssue)

    @Query("DELETE FROM drugissue")
    fun deleteAllDrugIssues()
}