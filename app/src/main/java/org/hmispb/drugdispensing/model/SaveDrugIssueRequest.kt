package org.hmispb.drugdispensing.model

data class SaveDrugIssueRequest(
    val hospitalCode : Int,
    val seatId : Int,
    val inputDataJson : String,
    val modeForData : String = "PATIENT_DRUG_ISSUE"
)