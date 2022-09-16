package org.hmispb.drugdispensing.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import org.hmispb.drugdispensing.DailyDrugConsumptionViewModel
import org.hmispb.drugdispensing.DrugViewModel
import org.hmispb.drugdispensing.R
import org.hmispb.drugdispensing.model.Data
import org.hmispb.drugdispensing.model.IssueDetail

class DrugDetailAdapter(private val data: Data,
    private val drugList: MutableList<IssueDetail>,
                        private val drugConsumptionViewModel: DailyDrugConsumptionViewModel,
                        private val drugViewModel: DrugViewModel
): RecyclerView.Adapter<DrugDetailAdapter.DrugDetailViewHolder>() {

    class DrugDetailViewHolder(view: View?) : RecyclerView.ViewHolder(view!!) {
        val delete : ImageView = view!!.findViewById(R.id.delete)
        val drugName: TextView? = view!!.findViewById(R.id.tv_drug_name)
        val issuedQuantity: TextView? = view!!.findViewById(R.id.tv_issued_quantity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrugDetailViewHolder {
        val adapterLayout =
            LayoutInflater.from(parent.context).inflate(R.layout.item_drug_details, parent, false)
        return DrugDetailViewHolder(adapterLayout)
    }

    override fun getItemCount() = drugList.size

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: DrugDetailViewHolder, position: Int) {
        val issueDetail = drugList[position]
        val drug = data.drugList.find {
            it.itemId == issueDetail.itemId.toInt()
        }
        holder.drugName?.text = drug?.drugName
        holder.issuedQuantity?.text = issueDetail.issueQty
        holder.delete.setOnClickListener{
           try {
                drugConsumptionViewModel.viewModelScope.launch {
                    drugConsumptionViewModel.updateDrugConsumption(
                        issueDetail.itemId.toLong(),
                        drugList[position].issueQty.toInt() * -1
                    )
                }
               drugViewModel.deleteAtIndex(position)

            } catch (e:Exception){
                e.printStackTrace()
            }
            drugList.removeAt(position)
            notifyDataSetChanged()
        }
    }

    fun updateData(newDrugList: MutableList<IssueDetail>) {
        if (newDrugList.isEmpty()){
            drugList.clear()
            notifyDataSetChanged()
        }
        else {
            if (drugList.isEmpty()) drugList.addAll(newDrugList)
            drugList.clear()
            drugList.addAll(newDrugList)
            notifyDataSetChanged()
        }
    }
}