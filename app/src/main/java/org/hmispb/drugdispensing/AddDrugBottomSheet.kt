package org.hmispb.drugdispensing

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
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

        val spinner = view.findViewById<MaterialTextView>(R.id.spinner_drug_name)
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

        spinner.setOnClickListener {
            val dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.dialog_searchable_spinner)
            dialog.window?.setLayout(650, 800)
            dialog.show()
            //Initiate and assign variable
            val editText = dialog.findViewById<EditText>(R.id.edit_text)
            val listView = dialog.findViewById<ListView>(R.id.list_view)
            //Array Adapter init
            val adapter: ArrayAdapter<String> = ArrayAdapter(requireContext(),
                android.R.layout.simple_list_item_1, drugs.map {
                    it.drugName
                })
            listView.adapter = adapter
            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    adapter.filter.filter(p0)
                }

                override fun afterTextChanged(p0: Editable?) = Unit

            })
            listView.setOnItemClickListener { p0, p1, p2, p3 ->
                drugViewModel.drugID.postValue(drugs[p2].itemId.toString())
                dialog.dismiss()
                spinner.text = adapter.getItem(p2)

            }
        }
        addDrugButton.setOnClickListener {
            try {
                if (spinner.text == "") Toast.makeText(
                    requireContext(),
                    "Please select drug",
                    Toast.LENGTH_SHORT
                ).show()
                else if (requestedQuantity.text?.isEmpty() == true) Toast.makeText(
                    requireContext(),
                    "Please add required quantity",
                    Toast.LENGTH_SHORT
                ).show()
                else {
                    drugViewModel.addQuantityToIssueDetail()
                    dismiss()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}