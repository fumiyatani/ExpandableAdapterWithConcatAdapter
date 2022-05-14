package com.ftani.expandablerecyclerviewsample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.ftani.expandablerecyclerviewsample.adapter.ExpandableAdapter
import com.ftani.expandablerecyclerviewsample.adapter.ExpandableHeader
import com.ftani.expandablerecyclerviewsample.adapter.ExpandableItem
import com.ftani.expandablerecyclerviewsample.adapter.Header
import com.ftani.expandablerecyclerviewsample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView(binding)
    }

    private fun setupRecyclerView(binding: ActivityMainBinding) {
        val expandableConcatAdapter = ConcatAdapter()

        val adapters = MutableList(10) {
            ExpandableAdapter(
                Header("ヘッダー$it"),
                ExpandableHeader(
                    "Expandableヘッダー$it",
                    isChecked = false,
                    isExpanded = true,
                    onSelectedHeader = {

                    }
                ),
                MutableList((1..4).random()) {
                    ExpandableItem(
                        "Expandableアイテム$it",
                        isChecked = false,
                        onSelectedItem = {}
                    )
                }
            )
        }
        adapters.forEach {
            expandableConcatAdapter.addAdapter(it)
        }
        binding.expandableRecyclerView.adapter = expandableConcatAdapter
        binding.expandableRecyclerView.layoutManager = LinearLayoutManager(this)
    }
}