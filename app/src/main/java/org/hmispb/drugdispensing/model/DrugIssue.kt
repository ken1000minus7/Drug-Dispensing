package org.hmispb.drugdispensing.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DrugIssue(
    @PrimaryKey(autoGenerate = true)
    val id : Int? = null,
    val crNo: Int,
    val hospitalId: Int? = null,
    val issueDetails: List<IssueDetail>,
    val patAge: String? = null,
    val patFName: String? =null,
    val patGender: String? = null,
    val patMobileNo: String? = null,
    val patName: String? = null
)