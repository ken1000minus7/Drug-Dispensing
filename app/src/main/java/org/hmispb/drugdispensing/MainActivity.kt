package org.hmispb.drugdispensing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
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
import com.google.gson.Gson
import org.hmispb.drugdispensing.model.Data

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val jsonString = resources!!.openRawResource(R.raw.data).bufferedReader().use { it.readText() }
        val data = Gson().fromJson(jsonString, Data::class.java)
        val viewModel : DrugViewModel by viewModels()
        val drugList = viewModel.issueDetails.value
        val adapter = DrugDetailAdapter(data,drugList?: mutableListOf())
        binding.recyclerView.adapter= adapter
        viewModel.issueDetails.observe(this) {
            adapter.updateData(it)
        }

        val addDrugFragment = AddDrugBottomSheet(data)
        binding.addDrugs.setOnClickListener {
            if (addDrugFragment.isAdded) return@setOnClickListener
            addDrugFragment.show(supportFragmentManager, "addDrugs")
        }
        binding.saveDrugDetails.setOnClickListener {
            if (binding.crNumberEditText.text.toString()=="") Toast.makeText(
                this,
                "Please enter CR number",
                Toast.LENGTH_SHORT
            ).show()
            viewModel.saveDrugs(binding.crNumberEditText.text.toString().toInt())
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.upload_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val view = LayoutInflater.from(this).inflate(R.layout.login_dialog,null,false)
        val dialog = AlertDialog.Builder(this)
            .setView(view)
            .create()
        dialog.setOnShowListener { dialogInterface ->
            val username = dialog.findViewById<EditText>(R.id.username)
            val password = dialog.findViewById<EditText>(R.id.password)
            val upload = dialog.findViewById<Button>(R.id.upload)
            upload?.setOnClickListener {
                if(username?.text.toString().isEmpty() || password?.text.isNullOrEmpty()) {
                    if(username?.text.toString().isEmpty())
                        username?.error = "Required"
                    if(password?.text.toString().isEmpty())
                        password?.error = "Required"
                    Toast.makeText(this@MainActivity,"One or more fields are empty", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
//                patientViewModel.upload(username!!.text.toString(),password!!.text.toString())
            }
//            patientViewModel.uploaded.observe(this@MainActivity) { uploaded ->
//                if(uploaded) {
//                    Toast.makeText(this@MainActivity,"Data successfully uploaded", Toast.LENGTH_SHORT).show()
//                    dialogInterface.cancel()
//                    patientViewModel.uploaded.value = false
//                }
//            }
        }
        dialog.show()
        return super.onOptionsItemSelected(item)
    }
}