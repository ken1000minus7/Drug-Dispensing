package org.hmispb.drugdispensing.model

data class IssueDetail(
    val batchNo: String = "",
    val issueQty: String = "",
    var itemId: String = "",
    val itembrandId: String = "",
    var requestedQty: String = "",
    val storeId: String = "",
    var unitId: String = ""
)