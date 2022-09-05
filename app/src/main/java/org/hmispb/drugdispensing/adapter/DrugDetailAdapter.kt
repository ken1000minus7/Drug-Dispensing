package org.hmispb.drugdispensing.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.hmispb.drugdispensing.R
import org.hmispb.drugdispensing.model.Data
import org.hmispb.drugdispensing.model.IssueDetail

class DrugDetailAdapter(private val data: Data,
    private val drugList: MutableList<IssueDetail>,
): RecyclerView.Adapter<DrugDetailAdapter.DrugDetailViewHolder>() {

    class DrugDetailViewHolder(view: View?) : RecyclerView.ViewHolder(view!!) {

        val drugName: TextView? = view!!.findViewById(R.id.tv_drug_name)
        val requestedQuantity: TextView? = view!!.findViewById(R.id.tv_requested_quantity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrugDetailViewHolder {
        val adapterLayout =
            LayoutInflater.from(parent.context).inflate(R.layout.item_drug_details, parent, false)
        return DrugDetailViewHolder(adapterLayout)
    }

    override fun getItemCount() = drugList.size

    override fun onBindViewHolder(holder: DrugDetailViewHolder, position: Int) {
        val issueDetail = drugList[position]
        val drug = data.drugList.find {
            it.itemId == issueDetail.itemId.toInt()
        }
        holder.drugName?.text = drug?.drugName
        holder.requestedQuantity?.text = issueDetail.requestedQty
    }

    fun updateData(newDrugList: MutableList<IssueDetail>) {
        if (drugList.isEmpty()) drugList.addAll(newDrugList)
        drugList.clear()
        drugList.addAll(newDrugList)
        notifyDataSetChanged()
    }
}