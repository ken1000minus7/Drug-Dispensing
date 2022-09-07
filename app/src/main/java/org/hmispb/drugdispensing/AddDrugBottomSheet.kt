package org.hmispb.drugdispensing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import org.hmispb.drugdispensing.model.Drug
import org.hmispb.drugdispensing.model.Data

@AndroidEntryPoint
class AddDrugBottomSheet(val data : Data) : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottomsheet_add_drug_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val spinner = view.findViewById<Spinner>(R.id.spinner_drug_name)
        val addDrugButton = view.findViewById<Button>(R.id.bottomSheet_addDrugs)
        val requestedQuantity = view.findViewById<TextInputEditText>(R.id.name_input)

        val drugViewModel: DrugViewModel by activityViewModels()
        val drugs = mutableListOf<Drug>()
        for (drug in data.drugList) {
            drugs.add(drug)
        }

        requestedQuantity.addTextChangedListener {
            drugViewModel.requestedQuantity.postValue(it.toString())
        }

        if (spinner != null) {
            val adapter = ArrayAdapter(requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                drugs.map {
                    it.drugName
                }
            )
            spinner.adapter = adapter
            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    drugViewModel.drugID.postValue(drugs[p2].itemId.toString())
                }

                override fun onNothingSelected(p0: AdapterView<*>?) = Unit
            }
        }
        addDrugButton.setOnClickListener {
            try {
                drugViewModel.addQuantityToIssueDetail()
                dismiss()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}