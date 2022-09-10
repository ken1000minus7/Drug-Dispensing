package org.hmispb.drugdispensing.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.hmispb.drugdispensing.model.DailyDrugConsumption

@Dao
interface DailyDrugConsumptionDao {

    @Query("SELECT * from DailyDrugConsumption")
    fun getDailyDrugConsumptions(): Flow<List<DailyDrugConsumption>>

    @Query("SELECT * from DailyDrugConsumption WHERE date =:date")
    fun searchDailyDrugConsumption(date: String): Flow<DailyDrugConsumption>

    @Insert
    suspend fun insertDailyDrugConsumption(dailyDrugConsumption: DailyDrugConsumption)

    @Query("UPDATE DailyDrugConsumption set drugList =:newDrugList where id =:id")
    suspend fun updateDailyDrugConsumption(newDrugList: List<Long>, id: String)
}