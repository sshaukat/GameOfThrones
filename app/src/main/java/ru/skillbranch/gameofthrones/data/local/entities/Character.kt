package ru.skillbranch.gameofthrones.data.local.entities

import androidx.room.*

data class Character(
    val id: String,
    val name: String,
    val gender: String,
    val culture: String,
    val born: String,
    val died: String,
    val titles: List<String> = listOf(),
    val aliases: List<String> = listOf(),
    val father: String, //rel
    val mother: String, //rel
    val spouse: String,
    val houseId: String//rel
)

data class CharacterItem(
    val id: String,
    val house: String, //rel
    val name: String,
    val titles: List<String>,
    val aliases: List<String>
)

data class CharacterFull(
    val id: String,
    val name: String,
    val words: String,
    val born: String,
    val died: String,
    val titles: List<String>,
    val aliases: List<String>,
    val house:String, //rel
    val father: RelativeCharacter?,
    val mother: RelativeCharacter?
)

data class RelativeCharacter(
    val id: String,
    val name: String,
    val house:String //rel
)

@Entity(tableName = "characters_general")
data class CharacterGeneral(
    @PrimaryKey val id: String,
    @ColumnInfo val name: String,
    @ColumnInfo val gender: String,
    @ColumnInfo val culture: String,
    @ColumnInfo val born: String,
    @ColumnInfo val died: String,
    @ColumnInfo @TypeConverters(ListStrConverters::class) val titles: List<String> = listOf(),
    @ColumnInfo @TypeConverters(ListStrConverters::class) val aliases: List<String> = listOf(),
    @ColumnInfo var father: String, //rel
    @ColumnInfo var mother: String, //rel
    @ColumnInfo val spouse: String,
    @ColumnInfo val houseId: String?,//rel
    @ColumnInfo val house: String,//rel
    @ColumnInfo val words: String
)

