package org.hmispb.drugdispensing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.hmispb.drugdispensing.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val viewModel : DrugViewModel by viewModels()
        val addDrugFragment = AddDrugBottomSheet()
        binding.addDrugs.setOnClickListener {
            if (addDrugFragment.isAdded) return@setOnClickListener
            addDrugFragment.show(supportFragmentManager, "addDrugs")
        }
        binding.saveDrugDetails.setOnClickListener {
            viewModel.saveDrugs(binding.crNumberEditText.text.toString().toInt())
        }
    }
}