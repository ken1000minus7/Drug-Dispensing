package org.hmispb.drugdispensing

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.hmispb.drugdispensing.model.DrugIssue
import org.hmispb.drugdispensing.model.IssueDetail
import org.hmispb.drugdispensing.model.LoginResponse
import org.hmispb.drugdispensing.room.DrugIssueRepository
import javax.inject.Inject

@HiltViewModel
class DrugViewModel  @Inject constructor(
    private val drugIssueRepository: DrugIssueRepository
) : ViewModel() {
    var uploaded : MutableLiveData<Boolean> = MutableLiveData(false)
    val issueDetails: MutableLiveData<MutableList<IssueDetail>> = MutableLiveData()
    val issueQuantity = MutableLiveData<String>()
    val drugID = MutableLiveData<String>()
    var drugIssueList = drugIssueRepository.getAllDrugIssues()

    fun addQuantityToIssueDetail() {
        if (issueQuantity.value!="") {
            val issueDetail = IssueDetail(
                issueQty = issueQuantity.value!!,
                itemId = drugID.value!!
            )
            val list = issueDetails.value ?: mutableListOf()
            list.add(issueDetail)
            issueDetails.postValue(list)
        }
    }
    fun deleteAtIndex(idx: Int){
        val list = issueDetails.value
        list?.removeAt(idx)
    }

    //Insert into local storage
    fun insertDrug(crNumber: String) {
        val drugIssue = DrugIssue(
            crNo = crNumber,
            issueDetails = issueDetails.value!!
        )
        viewModelScope.launch {
            drugIssueRepository.insertDrugIssue(drugIssue)
        }
        issueDetails.postValue(mutableListOf())
    }

    private fun saveDrugs(drugIssue: DrugIssue, hospitalCode: String, userId: String) {
        viewModelScope.launch {
            drugIssueRepository.saveDrugIssue(drugIssue,hospitalCode, userId)
        }
    }

    suspend fun login(username : String, password : String) : LoginResponse? =
        drugIssueRepository.login(username, password)

    private fun setUploaded(id : Int) {
        viewModelScope.launch(Dispatchers.IO) {
            drugIssueRepository.setUploaded(id)
        }
    }

    suspend fun containsNotUploaded() : Boolean {
        return drugIssueRepository.containsNotUploaded()
    }

    fun upload(username : String, password : String,drugIssues : List<DrugIssue>) {
        viewModelScope.launch {
            try {
                val response = login(username,password)
                for(drugIssue in drugIssues) {
                    if(response!=null && !drugIssue.isUploaded) {
                        try {
                            saveDrugs(drugIssue, response.dataValue!![0][0], response.dataValue[0][2])
                            setUploaded(drugIssue.id ?: 0)
                        } catch (e : Exception) {
                            e.printStackTrace()
                        }
                    }
                }
                uploaded.postValue(true)
            } catch (e : Exception) {
                e.printStackTrace()
            }
        }
    }
}