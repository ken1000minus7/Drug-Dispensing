package org.hmispb.drugdispensing

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import dagger.hilt.android.AndroidEntryPoint
import org.hmispb.drugdispensing.adapter.DrugDetailAdapter
import org.hmispb.drugdispensing.databinding.ActivityMainBinding
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import kotlinx.coroutines.launch
import org.hmispb.drugdispensing.model.DailyDrugConsumption
import org.hmispb.drugdispensing.model.Data

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var drugViewModel: DrugViewModel
    private lateinit var drugConsumptionViewModel: DailyDrugConsumptionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val jsonString = resources!!.openRawResource(R.raw.data).bufferedReader().use { it.readText() }
        val data = Gson().fromJson(jsonString, Data::class.java)

        drugViewModel = ViewModelProvider(this)[DrugViewModel::class.java]
        drugConsumptionViewModel = ViewModelProvider(this)[DailyDrugConsumptionViewModel::class.java]


        val allDailyDrugConsumptionList = drugConsumptionViewModel.getDailyDrugConsumptions()
        lifecycleScope.launch {
            allDailyDrugConsumptionList.collect {
                if (it.isEmpty()) {
                    drugConsumptionViewModel.insertDailyDrugConsumption(DailyDrugConsumption.firstRow)
                }
                drugConsumptionViewModel.insertEmptyDailyDrugConsumption()
            }
        }

        val drugList = drugViewModel.issueDetails.value
        val adapter = DrugDetailAdapter(data,drugList?: mutableListOf())
        binding.recyclerView.adapter= adapter
        drugViewModel.issueDetails.observe(this) {
            lifecycleScope.launch {
                    drugConsumptionViewModel.updateDrugConsumption(drugViewModel.drugID.value!!.toLong(), it.last().requestedQty.toInt())
            }
            adapter.updateData(it)
        }

        val addDrugFragment = AddDrugBottomSheet(data)
        binding.addDrugs.setOnClickListener {
            if (addDrugFragment.isAdded) return@setOnClickListener
            addDrugFragment.show(supportFragmentManager, "addDrugs")
        }

        binding.saveDrugDetails.setOnClickListener {
            if (binding.crNumberEditText.text.toString()=="") {
                Toast.makeText(
                    this,
                    "Please enter CR number",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            drugViewModel.insertDrug(binding.crNumberEditText.text.toString())
            Toast.makeText(this@MainActivity,"Prescription saved",Toast.LENGTH_SHORT).show()

        }

        drugViewModel.drugIssueList.observe(this) {
            Log.d("listy",it.toString())
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.upload_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.upload_button -> {
                val view = LayoutInflater.from(this).inflate(R.layout.login_dialog,null,false)
                val dialog = AlertDialog.Builder(this)
                    .setView(view)
                    .create()
                dialog.setOnShowListener { dialogInterface ->
                    val username = dialog.findViewById<EditText>(R.id.username)
                    val password = dialog.findViewById<EditText>(R.id.password)
                    val upload = dialog.findViewById<Button>(R.id.upload)
                    upload?.setOnClickListener {
                        if (username?.text.toString().isEmpty() || password?.text.isNullOrEmpty()) {
                            if (username?.text.toString().isEmpty())
                                username?.error = "Required"
                            if (password?.text.toString().isEmpty())
                                password?.error = "Required"
                            Toast.makeText(
                                this@MainActivity,
                                "One or more fields are empty",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@setOnClickListener
                        }
                        drugViewModel.drugIssueList.observe(this@MainActivity) { drugIssues ->
                            drugViewModel.upload(
                                username!!.text.toString(),
                                password!!.text.toString(),
                                drugIssues
                            )
                        }
                    }
                    drugViewModel.uploaded.observe(this@MainActivity) { uploaded ->
                        lifecycleScope.launch {
                            if (uploaded && dialog.isShowing) {
                                // TODO: Fix this
                                Toast.makeText(
                                    this@MainActivity,
                                    if (drugViewModel.containsNotUploaded()) "One or more entries were not uploaded" else "Data successfully uploaded",
                                    Toast.LENGTH_SHORT
                                ).show()
                                dialogInterface.cancel()
                                drugViewModel.uploaded.postValue(false)
                            }
                        }
                    }
                }
                dialog.show()
            }
            R.id.search_button -> {
                startActivity(Intent(this@MainActivity, SearchActivity::class.java))
            }
        }

        return super.onOptionsItemSelected(item)
    }
}