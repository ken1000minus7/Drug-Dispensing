package org.hmispb.drugdispensing.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.hmispb.drugdispensing.R
import org.hmispb.drugdispensing.Util.allDrugs
import org.hmispb.drugdispensing.model.DailyDrugConsumption
import org.hmispb.drugdispensing.model.DailyDrugConsumption.Companion.emptyDrugConsumptionItem
import org.hmispb.drugdispensing.model.Data

class DrugConsumptionAdapter(private val data: Data) :
 RecyclerView.Adapter<DrugConsumptionAdapter.DrugConsumptionViewHolder>() {

    private val drugs = mutableListOf<Long>()

    class DrugConsumptionViewHolder(view: View?) : RecyclerView.ViewHolder(view!!) {
        val drugName: TextView? = view!!.findViewById(R.id.drug_name_for_consumption)
        val count: TextView? = view!!.findViewById(R.id.count)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrugConsumptionViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_drug_consumption, parent, false)
        return DrugConsumptionViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: DrugConsumptionViewHolder, position: Int) {
        val item = drugs[position]
        val drug = data.drugList.find {
            it.itemId == allDrugs[position]
        }
        holder.drugName?.text = drug?.drugName
        holder.count?.text = item.toString()
    }

    override fun getItemCount(): Int = drugs.size

    fun updateData(newDrugList: DailyDrugConsumption) {
        if (newDrugList == emptyDrugConsumptionItem) {
            drugs.clear()
            notifyDataSetChanged()
        } else {
            if (drugs.isEmpty()) drugs.addAll(newDrugList.drugList)
            drugs.clear()
            drugs.addAll(newDrugList.drugList)
            notifyDataSetChanged()
        }
    }
}