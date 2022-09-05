package org.hmispb.drugdispensing.room

import androidx.lifecycle.LiveData
import com.google.gson.Gson
import org.hmispb.drugdispensing.model.DrugIssue
import org.hmispb.drugdispensing.model.SaveDrugIssueRequest

class DrugIssueRepositoryImpl(private val drugIssueDao: DrugIssueDao, private val drugIssueApi: DrugIssueApi) :
    DrugIssueRepository {

    override fun insertDrugIssue(drugIssue: DrugIssue) {
        drugIssueDao.insertDrugIssue(drugIssue)
    }

    override fun getAllDrugIssues(): LiveData<List<DrugIssue>> {
        return drugIssueDao.getAllDrugIssues()
    }

    override fun deleteDrugIssue(drugIssue: DrugIssue) {
        drugIssueDao.deleteDrugIssue(drugIssue)
    }

    override fun deleteAllDrugIssues() {
        drugIssueDao.deleteAllDrugIssues()
    }

    override suspend fun saveDrugIssue(drugIssue: DrugIssue) {
        val drugIssueString = Gson().toJson(drugIssue)
        // TODO : hospitalCode and seatId unknown
        val request = SaveDrugIssueRequest(0,0,drugIssueString)
        drugIssueApi.saveDrugIssue(request)
    }
}