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
import java.util.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var drugViewModel: DrugViewModel
    private lateinit var drugConsumptionViewModel: DailyDrugConsumptionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val appName = applicationInfo.loadLabel(packageManager).toString()
        title = "$appName v${BuildConfig.VERSION_NAME}"
        setContentView(binding.root)



        val jsonString = resources!!.openRawResource(R.raw.data).bufferedReader().use { it.readText() }
        val data = Gson().fromJson(jsonString, Data::class.java)
        val sharedPreferences = getSharedPreferences(Util.LOGIN_RESPONSE_PREF, MODE_PRIVATE)

        drugViewModel = ViewModelProvider(this)[DrugViewModel::class.java]
        drugConsumptionViewModel = ViewModelProvider(this)[DailyDrugConsumptionViewModel::class.java]
        val hospitalCode = sharedPreferences.getString(Util.HOSPITAL_CODE,"")
        val currentDate = Date()
        val currentMonth = currentDate.month+1
        val currentYear = currentDate.year + 1900
        val crMiddle = "${if(currentDate.date<10) "0" else ""}${currentDate.date}${if(currentMonth<10) "0" else ""}${currentMonth}${currentYear.toString().substring(2)}"
        binding.crStart.setText(hospitalCode)
        binding.crMid.setText(crMiddle)

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
        val adapter = DrugDetailAdapter(data,drugList?: mutableListOf(), drugConsumptionViewModel, drugViewModel)
        binding.recyclerView.adapter= adapter
        drugViewModel.issueDetails.observe(this) {
            lifecycleScope.launch {
               try{
                    if (it.isNotEmpty()) drugConsumptionViewModel.updateDrugConsumption(
                        drugViewModel.drugID.value!!.toLong(),
                        it.last().issueQty.toInt()

                    )
                   adapter.updateData(it)
                } catch (e: Exception){
                   Toast.makeText(this@MainActivity, "Please try again.", Toast.LENGTH_SHORT).show()
               }
            }

        }

        val addDrugFragment = AddDrugBottomSheet(data)
        binding.addDrugs.setOnClickListener {
            if (addDrugFragment.isAdded) return@setOnClickListener
            addDrugFragment.show(supportFragmentManager, "addDrugs")
        }

        binding.saveDrugDetails.setOnClickListener {
            if (binding.crno.text.toString() == "" || drugViewModel.issueDetails.value.isNullOrEmpty() ) {
                Toast.makeText(
                    this,
                    "Please enter CR number",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            else if ( adapter.itemCount==0) Toast.makeText(
                this,
                "Please add drugs",
                Toast.LENGTH_SHORT
            ).show()
            else {
                try {
                    drugViewModel.insertDrug(hospitalCode + crMiddle + binding.crno.text.toString())
                    Toast.makeText(this@MainActivity, "Prescription saved ", Toast.LENGTH_SHORT)
                        .show()
                    binding.crno.text?.clear()
                    adapter.updateData(mutableListOf())

                } catch (e: Exception) {
                    Toast.makeText(this, "Please enter drug details", Toast.LENGTH_SHORT).show()
                }
            }
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
                        if(!uploaded)
                            return@observe
                        drugViewModel.drugIssueList.observe(this) {
                            val drug = it.find { drug ->
                                !drug.isUploaded
                            }
                            if (drug==null) {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Data successfully uploaded",
                                    Toast.LENGTH_SHORT
                                ).show()
                                dialogInterface.cancel()
                            }
                            drugViewModel.uploaded.postValue(false)
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