package org.hmispb.drugdispensing

import android.os.Bundle
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.hmispb.drugdispensing.adapter.DrugConsumptionAdapter
import org.hmispb.drugdispensing.databinding.ActivitySearchBinding
import org.hmispb.drugdispensing.model.DailyDrugConsumption.Companion.emptyDrugConsumptionItem
import org.hmispb.drugdispensing.model.Data

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
       try { supportActionBar?.setDisplayHomeAsUpEnabled(true) } catch (e : Exception){}
        val viewModel = ViewModelProvider(this)[DailyDrugConsumptionViewModel::class.java]

        val jsonString =
            resources!!.openRawResource(R.raw.data).bufferedReader().use { it.readText() }

        val data = Gson().fromJson(jsonString, Data::class.java)

        val adapter = DrugConsumptionAdapter(data!!)

        binding.recyclerView.adapter = adapter
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            OnQueryTextListener {

            override fun onQueryTextSubmit(p0: String?): Boolean {
                lifecycleScope.launch {
                    try{ adapter.updateData(viewModel.searchDrugConsumptionOfAParticularDay(p0!!)) } catch (e: Exception){
                        Toast.makeText(
                            this@SearchActivity,
                            "Could not find data for specified date",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return true
            }
        })
        binding.searchView.setOnCloseListener {
            lifecycleScope.launch{
                adapter.updateData(emptyDrugConsumptionItem)
            }
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId== android.R.id.home){
            finish()
        }
        return true
    }
}