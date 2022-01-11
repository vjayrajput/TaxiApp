package com.taxi.app.data.repository

import com.taxi.app.data.local.dao.ItemDao
import com.taxi.app.data.model.db.Item
import com.taxi.app.data.remote.SafeApiRequest
import java.util.*
import javax.inject.Inject

class DatabaseRepository @Inject constructor(private val itemDao: ItemDao) :
    SafeApiRequest() {

    companion object {
        private val TAG: String = DatabaseRepository::class.java.simpleName
    }

    suspend fun getItemCount(): Int {
        return itemDao.getItemCount() ?: 0
    }

    suspend fun saveItem(item: Item): Long {
        return itemDao.insert(item) ?: 0
    }

    suspend fun getAllItems(): ArrayList<Item> {
        val list: List<Item>? = itemDao.getAllItems()
        if (list != null && list.isNotEmpty()) {
            return list as ArrayList<Item>
        }
        return ArrayList<Item>()
    }

}