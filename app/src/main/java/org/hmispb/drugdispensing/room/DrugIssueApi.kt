package org.hmispb.drugdispensing.room

import org.hmispb.drugdispensing.model.LoginRequest
import org.hmispb.drugdispensing.model.LoginResponse
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

    @Headers("Content-Type:text/plain")
    @POST("LoginAPI")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ) : LoginResponse?

}