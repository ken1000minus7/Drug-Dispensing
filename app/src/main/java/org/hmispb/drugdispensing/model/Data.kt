package org.hmispb.drugdispensing.model

data class Data(
    val ageUnit: List<AgeUnit>,
    val country: List<Country>,
    val district: List<District>,
    val drugDose: List<DrugDose>,
    val drugList: List<Drug>,
    val drugFrequency: List<DrugFrequency>,
    val gender: List<Gender>,
    val labTestName: List<LabTestName>,
    val state: List<State>
)