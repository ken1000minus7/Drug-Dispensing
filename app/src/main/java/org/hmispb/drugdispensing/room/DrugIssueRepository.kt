package org.hmispb.drugdispensing.room

import androidx.lifecycle.LiveData
import org.hmispb.drugdispensing.model.DrugIssue
import org.hmispb.drugdispensing.model.LoginResponse

interface DrugIssueRepository {
    suspend fun insertDrugIssue(drugIssue: DrugIssue)

    fun getAllDrugIssues(): LiveData<List<DrugIssue>>

    suspend fun deleteDrugIssue(drugIssue: DrugIssue)

    suspend fun deleteAllDrugIssues()

    suspend fun saveDrugIssue(drugIssue: DrugIssue, hospitalCode : String, userId : String)

    suspend fun login(username: String, password: String): LoginResponse?

    suspend fun setUploaded(id : Int)

    suspend fun containsNotUploaded() : Boolean
}