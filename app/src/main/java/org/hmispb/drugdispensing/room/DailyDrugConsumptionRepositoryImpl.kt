package org.hmispb.drugdispensing.room

import kotlinx.coroutines.flow.Flow
import org.hmispb.drugdispensing.model.DailyDrugConsumption

class DailyDrugConsumptionRepositoryImpl(private val dailyDrugConsumptionDao: DailyDrugConsumptionDao): DailyDrugConsumptionRepository {
    override suspend fun insertDailyDrugConsumption(dailyDrugConsumption: DailyDrugConsumption) {
        dailyDrugConsumptionDao.insertDailyDrugConsumption(dailyDrugConsumption)
    }

    override fun getDailyDrugConsumption(): Flow<List<DailyDrugConsumption>> {
        return dailyDrugConsumptionDao.getDailyDrugConsumptions()
    }

    override suspend fun updateDailyDrugConsumption(newDrugList: List<Long>, id: String) {
        dailyDrugConsumptionDao.updateDailyDrugConsumption(newDrugList, id)
    }

    override fun searchDailyDrugConsumption(date: String): Flow<DailyDrugConsumption> {
        return dailyDrugConsumptionDao.searchDailyDrugConsumption(date)
    }
}