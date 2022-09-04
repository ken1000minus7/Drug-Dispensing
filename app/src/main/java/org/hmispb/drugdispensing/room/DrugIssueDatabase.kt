package org.hmispb.drugdispensing.room

import androidx.room.Database
import androidx.room.RoomDatabase
import org.hmispb.drugdispensing.model.DrugIssue
import org.hmispb.drugdispensing.room.DrugIssueDao

@Database(
    entities = [DrugIssue::class],
    version = 1,
    exportSchema = false
)
abstract class DrugIssueDatabase : RoomDatabase() {
    abstract val drugIssueDao : DrugIssueDao
}