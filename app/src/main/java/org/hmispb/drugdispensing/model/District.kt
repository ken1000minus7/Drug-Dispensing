package org.hmispb.drugdispensing.model

data class District(
    val distID: Int,
    val distName: String,
    val stateId: Int,
    val stateName: String
)