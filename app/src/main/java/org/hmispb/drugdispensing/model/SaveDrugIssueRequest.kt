package org.hmispb.drugdispensing.model

data class SaveDrugIssueRequest(
    //TODO take hospital code and seat ID from Login api
    val hospitalCode : Int = 998,
    val seatId : Int = 10001,
    val inputDataJson : String,
    val modeForData : String = "PATIENT_DRUG_ISSUE"
)