package org.hmispb.drugdispensing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.hmispb.drugdispensing.model.Data

class AddDrugBottomSheet(val data : Data) : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottomsheet_add_drug_details, container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val drugs = mutableListOf<String>()
        for(drug in data.drugList)
        {
            drugs.add(drug.drugName)
        }

        val spinner = view.findViewById<Spinner>(R.id.spinner_drug_name)
        if (spinner!=null){
            val adapter = ArrayAdapter(requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                drugs
            )
            spinner.adapter = adapter
            spinner.onItemSelectedListener = object :
            AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    Toast.makeText(requireContext(), drugs[p2], Toast.LENGTH_SHORT).show()
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }

                override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    TODO("Not yet implemented")
                }

            }
        }
    }
}