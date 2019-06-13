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

package com.example.diceroller.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

/**
 * Defines methods for using the SleepNight class with Room.
 */
@Dao
interface RecordActivityDatabaseDao {

    @Insert
    fun insert(record: RecordActivity)

    /**
     * When updating a row with a value already set in a column,
     * replaces the old value with the new one.
     *
     * @param record new value to write
     */
    @Update
    fun update(record: RecordActivity)

    /**
     * Selects and returns the row that matches the supplied start time, which is our key.
     *
     * @param key startTimeMilli to match
     */
    @Query("SELECT * from record_table WHERE recordId = :key")
    fun get(key: Long): RecordActivity?

    /**
     * Deletes all values from the table.
     *
     * This does not delete the table, only its contents.
     */
    @Query("DELETE FROM record_table")
    fun clear()

    /**
     * Selects and returns all rows in the table,
     *
     * sorted by start time in descending order.
     */
    @Query("SELECT * FROM record_table ORDER BY recordId DESC")
    fun getAllRecords(): LiveData<List<RecordActivity>>

    /**
     * Selects and returns the latest night.
     */
    @Query("SELECT * FROM record_table ORDER BY recordId DESC LIMIT 1")
    fun getLastRecord(): RecordActivity?

}

