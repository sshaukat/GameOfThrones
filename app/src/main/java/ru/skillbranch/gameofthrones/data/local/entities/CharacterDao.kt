package ru.skillbranch.gameofthrones.data.local.entities

import androidx.room.*

@Dao
public interface CharacterDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(character: CharacterGeneral)

    @Query("delete from characters_general")
    suspend fun clear()

    @Query("select id, houseId as house, name, titles, aliases from characters_general where house=:houseName")
    suspend fun findCharactersByHouseName(houseName: String): List<CharacterItem>

    @Query("select id,name,words,born,died,titles,aliases,house,father,mother from characters_general where id=:characterId")
    suspend fun findCharacterFullById(characterId: String): List<CharacterFull>
}