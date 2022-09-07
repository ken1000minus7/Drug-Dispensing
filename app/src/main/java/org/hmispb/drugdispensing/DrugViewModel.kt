package org.hmispb.drugdispensing

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
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
    val issueDetails: MutableLiveData<MutableList<IssueDetail>> = MutableLiveData()
    val requestedQuantity = MutableLiveData<String>()
    val drugID = MutableLiveData<String>()

    fun addQuantityToIssueDetail() {
        val issueDetail = IssueDetail(
            requestedQty = requestedQuantity.value!!,
            itemId = drugID.value!!
        )
        val list = issueDetails.value ?: mutableListOf()
        list.add(issueDetail)
        issueDetails.postValue(list)
    }

    fun saveDrugs(crNumber: Int) {
        val drugIssue = DrugIssue(
            crNo = crNumber,
            issueDetails = issueDetails.value!!
        )
        viewModelScope.launch {
            drugIssueRepository.saveDrugIssue(drugIssue)
        }
    }

    suspend fun login(username : String, password : String) : LoginResponse? =
        drugIssueRepository.login(username, password)

}