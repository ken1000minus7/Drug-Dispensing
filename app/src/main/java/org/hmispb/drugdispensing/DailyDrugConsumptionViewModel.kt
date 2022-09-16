package org.hmispb.drugdispensing

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import org.hmispb.drugdispensing.model.DailyDrugConsumption
import org.hmispb.drugdispensing.room.DailyDrugConsumptionRepository
import java.util.*
import javax.inject.Inject

@HiltViewModel
class DailyDrugConsumptionViewModel @Inject constructor(
    private val dailyDrugConsumptionRepository: DailyDrugConsumptionRepository
) : ViewModel() {

    fun getDailyDrugConsumptions(): Flow<List<DailyDrugConsumption>> =
        dailyDrugConsumptionRepository.getDailyDrugConsumption()

    suspend fun insertDailyDrugConsumption(dailyDrugConsumption: DailyDrugConsumption) {
        dailyDrugConsumptionRepository.insertDailyDrugConsumption(dailyDrugConsumption)
    }

    suspend fun insertEmptyDailyDrugConsumption() {
        val allDailyDrugConsumption =
            dailyDrugConsumptionRepository.getDailyDrugConsumption().first().map {
                it.id
            }
        Log.d("whatthe", allDailyDrugConsumption.toString())

        val currentDate = Date()
        val currentMonth = currentDate.month+1
        val currentYear = currentDate.year + 1900
        val date = "${if(currentDate.date<10) "0" else ""}${currentDate.date}"
        val month = "${if(currentMonth<10) "0" else ""}${currentMonth}"
        val formattedID = "$currentYear$month$date"
        val formatted = "$date-$month-${currentYear.toString().substring(2)}"
        if (!allDailyDrugConsumption.contains(formattedID)) {
            val newDailyDrugConsumption = DailyDrugConsumption(id = formattedID,
                date = formatted,
                drugList = List(92) {
                    0
                })
            dailyDrugConsumptionRepository.insertDailyDrugConsumption(newDailyDrugConsumption)
        }
    }

    suspend fun updateDrugConsumption(drugId: Long, value: Int) {
        val currentDate = Date()
        val currentMonth = currentDate.month+1
        val currentYear = currentDate.year + 1900
        val date = "${if(currentDate.date<10) "0" else ""}${currentDate.date}"
        val month = "${if(currentMonth<10) "0" else ""}${currentMonth}"
        val formattedID = "$currentYear$month$date"
        val allDrugsOfToday =
            dailyDrugConsumptionRepository.getDailyDrugConsumption().first().last {
                it.id == formattedID
            }
        val druglist = allDrugsOfToday.drugList.toMutableList()
        val firstRow =
            dailyDrugConsumptionRepository.searchDailyDrugConsumption("date").first().drugList
        val toChangeIdx = firstRow.indexOf(drugId)
        druglist[toChangeIdx] += value.toLong()
        dailyDrugConsumptionRepository.updateDailyDrugConsumption(druglist, formattedID)
    }

    suspend fun searchDrugConsumptionOfAParticularDay(date: String): DailyDrugConsumption =
        dailyDrugConsumptionRepository.searchDailyDrugConsumption(date).first()

}
