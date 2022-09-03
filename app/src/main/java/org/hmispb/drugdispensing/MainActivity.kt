package org.hmispb.drugdispensing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.hmispb.drugdispensing.adapter.DrugDetailAdapter
import org.hmispb.drugdispensing.databinding.ActivityMainBinding
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recyclerView.adapter= DrugDetailAdapter(this)
        binding.recyclerView.setHasFixedSize(true)
        val addDrugs = findViewById<MaterialButton>(R.id.add_drugs)
        val addDrugFragment = AddDrugBottomSheet()
        addDrugs.setOnClickListener {
            if (addDrugFragment.isAdded) return@setOnClickListener
            addDrugFragment.show(supportFragmentManager, "addDrugs")
        }
    }




}