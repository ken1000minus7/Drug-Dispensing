package org.hmispb.drugdispensing.room

import androidx.lifecycle.LiveData
import org.hmispb.drugdispensing.model.DrugIssue
import org.hmispb.drugdispensing.model.LoginResponse

interface DrugIssueRepository {
    fun insertDrugIssue(drugIssue: DrugIssue)

    fun getAllDrugIssues(): LiveData<List<DrugIssue>>

    fun deleteDrugIssue(drugIssue: DrugIssue)

    fun deleteAllDrugIssues()

    suspend fun saveDrugIssue(drugIssue: DrugIssue)

    suspend fun login(username: String, password: String): LoginResponse?
}