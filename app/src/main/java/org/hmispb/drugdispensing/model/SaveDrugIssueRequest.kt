package org.hmispb.drugdispensing.model

data class SaveDrugIssueRequest(
    val hospitalCode : String,
    val seatId : String,
    val inputDataJson : String,
    val modeFordata : String = "PATIENT_DRUG_ISSUE"
)