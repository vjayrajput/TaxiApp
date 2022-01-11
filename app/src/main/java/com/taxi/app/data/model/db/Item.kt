package com.taxi.app.data.model.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "item")
class Item {

    @PrimaryKey(autoGenerate = true)
    @NotNull
    var id: Long? = null

    @ColumnInfo(name = "display_name")
    var displayName: String = ""

    @ColumnInfo(name = "version")
    var version: String = ""

}