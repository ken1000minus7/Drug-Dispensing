package org.hmispb.drugdispensing.room

import org.hmispb.drugdispensing.model.SaveDrugIssueRequest
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface DrugIssueApi {
    @Headers("Content-Type:text/plain")
    @POST("saveDrugIssued")
    suspend fun saveDrugIssue(
        @Body drugIssueRequest: SaveDrugIssueRequest
    )
}