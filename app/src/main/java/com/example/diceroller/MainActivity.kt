package com.example.diceroller

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.diceroller.database.RecordActivityDatabase


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: com.example.diceroller.databinding.ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        // Get a reference to the binding object and inflate the fragment views.

        val application = application

        val dataSource = RecordActivityDatabase.getInstance(application).sleepDatabaseDao

        val viewModelFactory = RecordViewModelFactory(dataSource, application)

        val sleepTrackerViewModel =
            ViewModelProviders.of(
                this, viewModelFactory).get(RecordViewModel::class.java)

        binding.viewmodel = sleepTrackerViewModel

        binding.setLifecycleOwner(this)
    }
}
