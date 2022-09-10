package org.hmispb.drugdispensing.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.hmispb.drugdispensing.model.DailyDrugConsumption
import org.hmispb.drugdispensing.model.DrugIssue
import org.hmispb.drugdispensing.room.DrugIssueDao

@Database(
    entities = [DrugIssue::class, DailyDrugConsumption::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(TypeConverter::class)
abstract class DrugIssueDatabase : RoomDatabase() {
    abstract val drugIssueDao: DrugIssueDao
    abstract val dailyDrugConsumptionDao: DailyDrugConsumptionDao
}