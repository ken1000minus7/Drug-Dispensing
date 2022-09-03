package org.hmispb.drugdispensing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val addDrugs = findViewById<MaterialButton>(R.id.add_drugs)
        val addDrugFragment = AddDrugBottomSheet()
        addDrugs.setOnClickListener {
            if (addDrugFragment.isAdded) return@setOnClickListener
            addDrugFragment.show(supportFragmentManager, "addDrugs")
        }
    }
}