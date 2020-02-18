package ru.skillbranch.gameofthrones.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "houses")
data class House(
    @PrimaryKey val id: String,
    @ColumnInfo val name: String,
    @ColumnInfo val region: String,
    @ColumnInfo val coatOfArms: String,
    @ColumnInfo val words: String,
    @ColumnInfo val titles: List<String>,
    @ColumnInfo val seats: List<String>,
    @ColumnInfo val currentLord: String, //rel
    @ColumnInfo val heir: String, //rel
    @ColumnInfo val overlord: String,
    @ColumnInfo val founded: String,
    @ColumnInfo val founder: String, //rel
    @ColumnInfo val diedOut: String,
    @ColumnInfo val ancestralWeapons: List<String>
)