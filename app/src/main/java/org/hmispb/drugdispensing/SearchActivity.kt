package org.hmispb.drugdispensing

import android.os.Bundle
import android.util.Log
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
        val appName = applicationInfo.loadLabel(packageManager).toString()
        title = "$appName v${BuildConfig.VERSION_NAME}"
        setContentView(binding.root)
        try { supportActionBar?.setDisplayHomeAsUpEnabled(true) } catch (e : Exception){}
        val viewModel = ViewModelProvider(this)[DailyDrugConsumptionViewModel::class.java]

        val jsonString =
            resources!!.openRawResource(R.raw.data).bufferedReader().use { it.readText() }

        val data = Gson().fromJson(jsonString, Data::class.java)

        val adapter = DrugConsumptionAdapter(data!!)

        binding.recyclerView.adapter = adapter

        binding.search.setOnClickListener {
            Log.d("hello","check")
            if(binding.date.text.toString().length<2 || binding.month.toString().length<2 || binding.year.toString().length<2) {
                if(binding.date.text.toString().length<2)
                    binding.date.error = "Required"
                if(binding.month.text.toString().length<2)
                    binding.month.error = "Required"
                if(binding.year.text.toString().length<2)
                    binding.year.error = "Required"
                Log.d("hello","again")
                return@setOnClickListener
            }
            val date = "${binding.date.text.toString()}-${binding.month.text.toString()}-${binding.year.text.toString()}"
            Log.d("hello",date)
            lifecycleScope.launch {
                try{ adapter.updateData(viewModel.searchDrugConsumptionOfAParticularDay(date)) } catch (e: Exception){
                    Toast.makeText(
                        this@SearchActivity,
                        "Could not find data for specified date",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId== android.R.id.home){
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}