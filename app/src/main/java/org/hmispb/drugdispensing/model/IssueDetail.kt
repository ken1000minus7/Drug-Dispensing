package org.hmispb.drugdispensing.model

data class IssueDetail(
    val batchNo: String,
    val issueQty: String,
    val itemId: String,
    val itembrandId: String,
    val requestedQty: String,
    val storeId: String,
    val unitId: String
)