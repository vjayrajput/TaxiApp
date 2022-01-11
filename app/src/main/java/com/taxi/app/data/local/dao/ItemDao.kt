package com.taxi.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.taxi.app.data.model.db.Item

@Dao
interface ItemDao {

    @Query("SELECT COUNT(id) FROM item")
    suspend fun getItemCount(): Int?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: Item): Long?

    @Query("SELECT * FROM item")
    suspend fun getAllItems(): List<Item>?

}