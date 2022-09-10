package org.hmispb.drugdispensing.room

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.hmispb.drugdispensing.model.IssueDetail
import java.lang.reflect.Type

object TypeConverter {

    @TypeConverter
    fun issueDetailsToJSON(issueDetails: List<IssueDetail>) = Gson().toJson(issueDetails)!!

    @TypeConverter
    fun issueDetailsFromJSON(issueDetailsJSON: String): List<IssueDetail>{
        val type: Type = object: TypeToken<ArrayList<IssueDetail>>() {}.type
        return Gson().fromJson(
            issueDetailsJSON,
            type
        ) ?: emptyList()
    }

    @TypeConverter
    fun drugListToJSON(drugList: List<Long>) = Gson().toJson(drugList)!!

    @TypeConverter
    fun drugListFromJson(drugListJson: String) :List<Long> {
        val type: Type = object  : TypeToken<ArrayList<Long>>() {}.type
        return Gson().fromJson(
            drugListJson,
            type
        ) ?: emptyList()
    }
}