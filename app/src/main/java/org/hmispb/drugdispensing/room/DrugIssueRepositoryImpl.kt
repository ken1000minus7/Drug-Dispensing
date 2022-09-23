package org.hmispb.drugdispensing.room

import android.util.Log
import androidx.lifecycle.LiveData
import com.google.gson.Gson
import org.hmispb.drugdispensing.model.DrugIssue
import org.hmispb.drugdispensing.model.LoginRequest
import org.hmispb.drugdispensing.model.LoginResponse
import org.hmispb.drugdispensing.model.SaveDrugIssueRequest

class DrugIssueRepositoryImpl(private val drugIssueDao: DrugIssueDao, private val drugIssueApi: DrugIssueApi) :
    DrugIssueRepository {

    override suspend fun insertDrugIssue(drugIssue: DrugIssue) {
        drugIssueDao.insertDrugIssue(drugIssue)
    }

    override fun getAllDrugIssues(): LiveData<List<DrugIssue>> {
        return drugIssueDao.getAllDrugIssues()
    }

    override suspend fun deleteDrugIssue(drugIssue: DrugIssue) {
        drugIssueDao.deleteDrugIssue(drugIssue)
    }

    override suspend fun deleteAllDrugIssues() {
        drugIssueDao.deleteAllDrugIssues()
    }

    override suspend fun saveDrugIssue(drugIssue: DrugIssue, hospitalCode: String, userId: String) {
        val drugIssueString = Gson().toJson(drugIssue)
        val request = SaveDrugIssueRequest(hospitalCode,userId,drugIssueString)
        Log.d("checky",Gson().toJson(request))
        drugIssueApi.saveDrugIssue(request)
    }

    override suspend fun login(username: String, password: String): LoginResponse? {
        return drugIssueApi.login(LoginRequest(listOf(username,password)))
    }

    override suspend fun setUploaded(id: Int) {
        drugIssueDao.setUploaded(id)
    }

    override suspend fun containsNotUploaded(): Boolean {
        Log.d("sadness",drugIssueDao.notUploadedCount().toString())
        return drugIssueDao.notUploadedCount() > 0
    }
}