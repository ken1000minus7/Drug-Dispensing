package org.hmispb.drugdispensing.room

import kotlinx.coroutines.flow.Flow
import org.hmispb.drugdispensing.model.DailyDrugConsumption

interface DailyDrugConsumptionRepository {

    suspend fun insertDailyDrugConsumption(dailyDrugConsumption: DailyDrugConsumption)
    fun getDailyDrugConsumption(): Flow<List<DailyDrugConsumption>>
    suspend fun updateDailyDrugConsumption(newDrugList: List<Long>, id: String)
    fun searchDailyDrugConsumption(date: String): Flow<DailyDrugConsumption>
}