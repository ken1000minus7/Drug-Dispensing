package org.hmispb.drugdispensing

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import org.hmispb.drugdispensing.model.DailyDrugConsumption
import org.hmispb.drugdispensing.room.DailyDrugConsumptionRepository
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject


@RequiresApi(Build.VERSION_CODES.O)
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

        val current = LocalDateTime.now()
        val currentDateIntoID = DateTimeFormatter.BASIC_ISO_DATE
        val formattedID = current.format(currentDateIntoID)
        val currentDate = DateTimeFormatter.ofPattern("dd-MM-yy")
        val formatted = current.format(currentDate)
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
        val current = LocalDateTime.now()
        val currentDateIntoID = DateTimeFormatter.BASIC_ISO_DATE
        val formattedID = current.format(currentDateIntoID)
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
