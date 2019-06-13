/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.diceroller

import android.app.Application
import androidx.databinding.Bindable
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableInt
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.diceroller.database.RecordActivity
import com.example.diceroller.database.RecordActivityDatabaseDao
import kotlinx.coroutines.*

/**
 * ViewModel for SleepTrackerFragment.
 */
class RecordViewModel(
        val database: RecordActivityDatabaseDao,
        application: Application) : AndroidViewModel(application) {

    /**
     * viewModelJob allows us to cancel all coroutines started by this ViewModel.
     */
    private var viewModelJob = Job()

    /**
     * A [CoroutineScope] keeps track of all coroutines started by this ViewModel.
     *
     * Because we pass it [viewModelJob], any coroutine started in this uiScope can be cancelled
     * by calling `viewModelJob.cancel()`
     *
     * By default, all coroutines started in uiScope will launch in [Dispatchers.Main] which is
     * the main thread on Android. This is a sensible default because most coroutines started by
     * a [ViewModel] update the UI after performing some processing.
     */
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var record = MutableLiveData<RecordActivity?>()

    private val records = database.getAllRecords()

    var activityRegister = MutableLiveData<String>()
    var hours = MutableLiveData<String>()
    var listProjects = MutableLiveData<ArrayList<String>>()
    var positionSpinner = MutableLiveData<Int>()


    /**
     * Request a toast by setting this value to true.
     *
     * This is private because we don't want to expose setting this value to the Fragment.
     */
    private var _showSnackbarEvent = MutableLiveData<Boolean>()

    /**
     * If this is true, immediately `show()` a toast and call `doneShowingSnackbar()`.
     */
    val showSnackBarEvent: LiveData<Boolean>
        get() = _showSnackbarEvent


    /**
     * Call this immediately after calling `show()` on a toast.
     *
     * It will clear the toast request, so if the user rotates their phone it won't show a duplicate
     * toast.
     */

    fun doneShowingSnackbar() {
        _showSnackbarEvent.value = false
    }



    init {
        val arrayList = ArrayList<String>()//Creating an empty arraylist
        arrayList.add("Project 1")//Adding object in arraylist
        arrayList.add("Project 2")
        arrayList.add("Project 3")
        arrayList.add("Project 4")
        arrayList.add("Project 5")
        listProjects.value = arrayList
    }

    private suspend fun insert(night: RecordActivity) {
        withContext(Dispatchers.IO) {
            database.insert(night)
        }
    }

    /**
     * Executes when the START button is clicked.
     */
    fun saveRecord() {
        uiScope.launch {
            // Create a new night, which captures the current time,
            // and insert it into the database.
            val newRecord = RecordActivity()
            newRecord.activity = activityRegister.value.toString()
            newRecord.hours = hours.value?.toInt() ?: 0
            newRecord.project = listProjects.value?.get(positionSpinner.value!!).toString()
            insert(newRecord)
            hours.value = ""
            activityRegister.value = ""
            positionSpinner.value = 0
            //record.value = getRecordFromDatabase()

        }
    }


    /**
     * Called when the ViewModel is dismantled.
     * At this point, we want to cancel all coroutines;
     * otherwise we end up with processes that have nowhere to return to
     * using memory and resources.
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
