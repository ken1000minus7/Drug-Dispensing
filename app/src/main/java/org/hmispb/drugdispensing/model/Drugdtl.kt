package org.hmispb.drugdispensing.model

data class Drugdtl(
    val dosageId : String,
    val drugId: String,
    val frequencyId : String,
    val instrunction: String,
    val noOfdays : String
)