package org.hmispb.drugdispensing.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.hmispb.drugdispensing.R

class DrugDetailAdapter(
    private val context: Context?
): RecyclerView.Adapter<DrugDetailAdapter.DrugDetailViewHolder>() {
    // Initialize the data using the List found in data/DataSource

    private val drugList = listOf(DummyDrug(),DummyDrug(),DummyDrug(),DummyDrug(),DummyDrug(),DummyDrug(),DummyDrug(),DummyDrug(),DummyDrug(),DummyDrug(),)
    /**
     * Initialize view elements
     */
    class DrugDetailViewHolder(view: View?): RecyclerView.ViewHolder(view!!) {

        val drugName: TextView? = view!!.findViewById(R.id.tv_drug_name)
        val requestedQuantity: TextView? = view!!.findViewById(R.id.tv_requested_quantity)
        val expiryDate: TextView? = view!!.findViewById(R.id.tv_expiry_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrugDetailViewHolder {
        //  Use a conditional to determine the layout type and set it accordingly.
        //  if the layout variable is Layout.GRID the grid list item should be used. Otherwise the
        //  the vertical/horizontal list item should be used.
        val adapterLayout = LayoutInflater.from(parent.context).inflate(R.layout.item_drug_details, parent, false)

        //  Null should not be passed into the view holder. This should be updated to reflect
        //  the inflated layout.
        return DrugDetailViewHolder(adapterLayout)
    }

    override fun getItemCount(): Int {
        return drugList.size
    } // return the size of the data set

    override fun onBindViewHolder(holder: DrugDetailViewHolder, position: Int) {
        // Get the data at the current position
        val drug = drugList[position]
        // Set the text for the current dog's name
        holder.drugName?.text = drug.name
        holder.requestedQuantity?.text = drug.requested_quantity
        holder.expiryDate?.text = drug.expiryDate
    }

}

data class DummyDrug(
    val name: String = "Drug 1",
    val requested_quantity: String = "1",
    val expiryDate: String = "121212"
)
